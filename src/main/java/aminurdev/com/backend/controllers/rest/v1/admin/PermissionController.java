package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.Permission;
import aminurdev.com.backend.domain.request.PermissionRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.PermissionService;
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
@RequestMapping("/api/v1/admin/permission")
@RequiredArgsConstructor
@Tag(name = "User Management/Permission", description = "Operations related to managing permission")
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    @Operation(summary = "1. Get all data with pagination", description = "Retrieve a list of permissions with pagination.")
    public ResponseEntity<PaginationResponse<Permission>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    )
    {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Permission> paginationResponse = permissionService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all-permission")
    @Operation(summary = "2. Get all permissions", description = "Retrieve a list of all permissions without pagination.")
    public ResponseEntity<GlobalResponse> getAllPermission()
    {

        GlobalResponse globalResponse = new GlobalResponse().success(
                permissionService.getAllPermission(),
                "All permission fetch successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(globalResponse);
    }

    @PostMapping
    @Operation(summary = "3. Store a new permission", description = "Create a new permission.")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody PermissionRequest request)
    {
        try {

            Permission permission = permissionService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    permission,
                    "Store successful",
                    true,
                    HttpStatus.CREATED.value()
            );

            return ResponseEntity.status(HttpStatus.CREATED.value()).body(globalResponse);
        }catch (Exception exception){

            GlobalResponse responseWrapper = new GlobalResponse().error(
                    Collections.singletonList(exception.getMessage()),
                    "Failed",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(responseWrapper);

        }
    }

    @GetMapping("{id}")
    @Operation(summary = "4. Get permission by ID", description = "Returns a single permission by their ID")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer permissionId)
    {
        try{

            Permission permission = permissionService.edit(permissionId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    permission,
                    "Permission fetch successful",
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
    @Operation(summary = "5. Update a permission", description = "Update an existing permission by its ID.")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer permissionId, @Valid @RequestBody PermissionRequest request)
    {
        try {

            Permission permission = permissionService.update(request, permissionId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    permission,
                    "Update successful",
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

    @DeleteMapping("{id}")
    @Operation(summary = "6. Delete a permission", description = "Delete a permission by its ID.")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer permissionId)
    {
        try{

            permissionService.destroy(permissionId);

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
