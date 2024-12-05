package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.request.UserRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/user")
@RequiredArgsConstructor
@Tag(name="User Management/User", description = "Operations related to managing user")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "1. Get all user pagination", description = "Retrieve a list of all user with pagination.")
    public ResponseEntity<PaginationResponse<User>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    )
    {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<User> paginationResponse = userService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all-users")
    @Operation(summary = "2. Get all users", description = "Retrieve a list of all user without pagination.")
    public ResponseEntity<GlobalResponse> getAllUsers()
    {
        List<User> user = userService.getAllUsers();

        GlobalResponse response = new GlobalResponse().success(
                user,
                "Get all user successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @PostMapping
    @Operation(summary = "3. store user", description = "Creating new user")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody UserRequest request)
    {
        try {

            User user = userService.store(request);

            GlobalResponse response = new GlobalResponse().success(
                    user,
                    "Store successful",
                    true,
                    HttpStatus.CREATED.value()
            );

            return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);

        }catch (Exception exception){

            GlobalResponse response = new GlobalResponse().success(
                    Collections.singletonList(exception.getMessage()),
                    "Store successful",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("{id}")
    @Operation(summary = "4. Get user by ID", description = "Returns a single user by their ID")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer userId)
    {
        try{

            User user = userService.edit(userId);

            GlobalResponse response = new GlobalResponse().success(
                    user,
                    "Get user by id successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(response);
        }catch (RequestRejectedException exception){

            GlobalResponse responseWrapper = new GlobalResponse().success(
                    Collections.singletonList(exception.getMessage()),
                    "Record Not Found",
                    false,
                    HttpStatus.NOT_FOUND.value()
            );

            return ResponseEntity.ok(responseWrapper);
        }catch (Exception exception){

            GlobalResponse responseWrapper = new GlobalResponse().success(
                    Collections.singletonList(exception.getMessage()),
                    "Failed, server error",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.ok(responseWrapper);
        }
    }

    @PutMapping("{id}")
    @Operation(summary = "5. update user", description = "Update an existing user by its ID.")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer userId, @Valid @RequestBody UserRequest request)
    {
        try{

            User user = userService.update(request, userId);

            GlobalResponse response = new GlobalResponse().success(
                    user,
                    "Update successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(response);

        }catch (RequestRejectedException exception){

            GlobalResponse responseWrapper = new GlobalResponse().success(
                    Collections.singletonList(exception.getMessage()),
                    "Record Not Found",
                    false,
                    HttpStatus.NOT_FOUND.value()
            );

            return ResponseEntity.ok(responseWrapper);

        }catch (Exception exception){

            GlobalResponse responseWrapper = new GlobalResponse().success(
                    Collections.singletonList(exception.getMessage()),
                    "Failed, server error",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.ok(responseWrapper);
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "6. Delete a user", description = "Delete a user by its ID.")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer userId)
    {
        try{

            userService.destroy(userId);

            GlobalResponse response = new GlobalResponse().success(
                    Collections.singletonList(""),
                    "Delete successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(response);
        }catch (RequestRejectedException exception){

            GlobalResponse responseWrapper = new GlobalResponse().success(
                    Collections.singletonList(exception.getMessage()),
                    "Record Not Found",
                    false,
                    HttpStatus.NOT_FOUND.value()
            );

            return ResponseEntity.ok(responseWrapper);

        }catch (Exception exception){

            GlobalResponse responseWrapper = new GlobalResponse().success(
                    Collections.singletonList(exception.getMessage()),
                    "Failed, server error",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.ok(responseWrapper);
        }
    }
}
