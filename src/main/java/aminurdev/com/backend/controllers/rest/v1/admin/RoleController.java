package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.Permission;
import aminurdev.com.backend.domain.entity.Role;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.request.RoleRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/api/v1/admin/role")
@RequiredArgsConstructor
@Tag(name = "User Management/Role", description = "Operations related to managing role")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @Operation(summary = "1. Get all roles pagination", description = "Retrieve a list of all role with pagination.")
    public ResponseEntity<PaginationResponse<Role>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    )
    {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Role> paginationResponse = roleService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all-roles")
    @Operation(summary = "2. Get all roles", description = "Retrieve a list of all role without pagination.")
    public ResponseEntity<GlobalResponse> getAllRoles()
    {
        List<Role> roles = roleService.getAllRoles();

        GlobalResponse globalResponse = new GlobalResponse().success(
                roles,
                "Fetch all roles successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);
    }

    @PostMapping
    @Operation(summary = "3. store role", description = "Create new role")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody RoleRequest request)
    {
        try {

            Role role = roleService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    role,
                    "Store successful",
                    true,
                    HttpStatus.CREATED.value()
            );

            return ResponseEntity.status(HttpStatus.CREATED.value()).body(globalResponse);
        }catch (Exception exception){

            GlobalResponse globalResponse = new GlobalResponse().success(
                    Collections.singletonList(exception.getMessage()),
                    "failed to store",
                    true,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(globalResponse);
        }
    }

    @GetMapping("{id}")
    @Operation(summary = "4. Get role by ID", description = "Returns a single role by their ID")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer roleId)
    {
        try{

            GlobalResponse globalResponse = new GlobalResponse().success(
                    roleService.edit(roleId),
                    "role fetch successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);
        }catch (Exception exception){

            GlobalResponse globalResponse = new GlobalResponse().success(
                    Collections.singletonList(exception.getMessage()),
                    "Server error",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(globalResponse);
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "5. update role", description = "Update an existing role by its ID.")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer roleId, @Valid @RequestBody RoleRequest request)
    {
        try {

            Role role = roleService.update(request, roleId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    role,
                    "Update successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);

        }catch (Exception exception){

            GlobalResponse globalResponse = new GlobalResponse().success(
                    Collections.singletonList(exception.getMessage()),
                    "Server error",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(globalResponse);
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "6. Delete a role", description = "Delete a role by its ID.")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer roleId)
    {
        try{

            roleService.destroy(roleId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    Collections.singletonList(""),
                    "Destroy successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);

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
