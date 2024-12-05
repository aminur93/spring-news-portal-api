package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.Menu;
import aminurdev.com.backend.domain.entity.Permission;
import aminurdev.com.backend.domain.request.MenuRequest;
import aminurdev.com.backend.domain.request.UserRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/api/v1/admin/menu")
@RequiredArgsConstructor
@Tag(name = "Menu Management", description = "Operations related to managing menus and their hierarchy")
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    @Operation(
            summary = "Get paginated list of menus",
            description = "Retrieves a paginated list of menus along with their parent-child hierarchy."
    )
    public ResponseEntity<PaginationResponse<Menu>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    )
    {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Menu> paginationResponse = menuService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all-menus")
    @Operation(
            summary = "Get list of all menus",
            description = "Retrieves a list of all menus along with their parent-child hierarchy."
    )
    public ResponseEntity<GlobalResponse> getAllMenus()
    {
        List<Menu> menus = menuService.getAllMenus();

        GlobalResponse globalResponse = new GlobalResponse().success(
                menus,
                "All menu fetch successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);
    }

    @PostMapping
    @Operation(
            summary = "Create a new menu",
            description = "Stores a new menu item and optionally assigns a parent for hierarchical structure."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody MenuRequest request)
    {
        try{

            Menu menu = menuService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    menu,
                    "Store successful",
                    true,
                    HttpStatus.CREATED.value()
            );

            return ResponseEntity.status(HttpStatus.CREATED.value()).body(globalResponse);

        }catch (Exception exception){

            GlobalResponse globalResponse = new GlobalResponse().error(
                    Collections.singletonList(exception.getMessage()),
                    "Failed server error",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(globalResponse);
        }
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Edit an existing menu",
            description = "Updates an existing menu item based on the provided ID and new details."
    )
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer menuId)
    {
        try {

            Menu menu = menuService.edit(menuId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    menu,
                    "Fetch menu by id successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);

        }catch (RequestRejectedException exception){

            GlobalResponse globalResponse = new GlobalResponse().error(
                    Collections.singletonList(exception.getMessage()),
                    "Failed server error",
                    false,
                    HttpStatus.NOT_FOUND.value()
            );

            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(globalResponse);

        }catch (Exception exception){

            GlobalResponse globalResponse = new GlobalResponse().error(
                    Collections.singletonList(exception.getMessage()),
                    "Failed server error",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(globalResponse);

        }
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Update an existing menu",
            description = "Updates an existing menu item based on the provided ID and the new details."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer menuId, @Valid @RequestBody MenuRequest request)
    {
        try {

            Menu menu = menuService.update(menuId, request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    menu,
                    "Fetch menu by id successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);

        }catch (RequestRejectedException exception){

            GlobalResponse globalResponse = new GlobalResponse().error(
                    Collections.singletonList(exception.getMessage()),
                    "Failed server error",
                    false,
                    HttpStatus.NOT_FOUND.value()
            );

            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(globalResponse);

        }catch (Exception exception){

            GlobalResponse globalResponse = new GlobalResponse().error(
                    Collections.singletonList(exception.getMessage()),
                    "Failed server error",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(globalResponse);

        }
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete an existing menu",
            description = "Deletes an existing menu item based on the provided ID."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer menuId)
    {
        try {

            menuService.destroy(menuId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    "",
                    "Delete successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);

        }catch (RequestRejectedException exception){

            GlobalResponse globalResponse = new GlobalResponse().error(
                    Collections.singletonList(exception.getMessage()),
                    "Failed server error",
                    false,
                    HttpStatus.NOT_FOUND.value()
            );

            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(globalResponse);

        }catch (Exception exception){

            GlobalResponse globalResponse = new GlobalResponse().error(
                    Collections.singletonList(exception.getMessage()),
                    "Failed server error",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(globalResponse);

        }
    }
}
