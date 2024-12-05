package aminurdev.com.backend.controllers.rest.v1.admin;


import aminurdev.com.backend.domain.entity.Division;
import aminurdev.com.backend.domain.request.DivisionRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.DivisionService;
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
@RequestMapping("/api/v1/admin/division")
@RequiredArgsConstructor
@Tag(name = "Division Api", description = "Operations related to managing divisions")
public class DivisionController {

    private final DivisionService divisionService;

    @GetMapping
    @Operation(summary = "1. Get all divisions with pagination", description = "Retrieve a list of all divisions with pagination.")
    public ResponseEntity<PaginationResponse<Division>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Division> paginationResponse = divisionService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all-divisions")
    @Operation(summary = "2. Get all divisions", description = "Retrieve a list of all divisions without pagination.")
    public ResponseEntity<GlobalResponse> getAllDivisions() {
        List<Division> divisions = divisionService.getAllDivisions();

        GlobalResponse globalResponse = new GlobalResponse().success(
                divisions,
                "Fetch all divisions successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);
    }

    @PostMapping
    @Operation(summary = "3. Store a division", description = "Create a new division")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody DivisionRequest request) {
        try {
            Division division = divisionService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    division,
                    "Store successful",
                    true,
                    HttpStatus.CREATED.value()
            );

            return ResponseEntity.status(HttpStatus.CREATED.value()).body(globalResponse);

        } catch (Exception exception) {
            GlobalResponse globalResponse = new GlobalResponse().success(
                    Collections.singletonList(exception.getMessage()),
                    "Failed to store",
                    true,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(globalResponse);
        }
    }

    @GetMapping("{id}")
    @Operation(summary = "4. Get division by ID", description = "Returns a single division by its ID")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer divisionId) {
        try {
            Division division = divisionService.edit(divisionId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    division,
                    "Division fetch successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);

        } catch (Exception exception) {
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
    @Operation(summary = "5. Update a division", description = "Update an existing division by its ID.")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer divisionId, @Valid @RequestBody DivisionRequest request) {
        try {
            Division division = divisionService.update(request, divisionId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    division,
                    "Update successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);

        } catch (Exception exception) {
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
    @Operation(summary = "6. Delete a division", description = "Delete a division by its ID.")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer divisionId) {
        try {
            divisionService.destroy(divisionId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    Collections.singletonList(""),
                    "Destroy successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);

        } catch (Exception exception) {
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
