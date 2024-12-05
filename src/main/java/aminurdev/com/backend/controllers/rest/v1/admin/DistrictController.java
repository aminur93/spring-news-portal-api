package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.District;
import aminurdev.com.backend.domain.request.DistrictRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.DistrictService;
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
@RequestMapping("/api/v1/admin/district")
@RequiredArgsConstructor
@Tag(name = "District Api", description = "Operations related to managing districts")
public class DistrictController {

    private final DistrictService districtService;

    @GetMapping
    @Operation(summary = "1. Get all districts with pagination", description = "Retrieve a list of all districts with pagination.")
    public ResponseEntity<PaginationResponse<District>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<District> paginationResponse = districtService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all-districts")
    @Operation(summary = "2. Get all districts", description = "Retrieve a list of all districts without pagination.")
    public ResponseEntity<GlobalResponse> getAllDistricts() {
        List<District> districts = districtService.getAllDistricts();

        GlobalResponse globalResponse = new GlobalResponse().success(
                districts,
                "Fetch all districts successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);
    }

    @PostMapping
    @Operation(summary = "3. Store a district", description = "Create a new district")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody DistrictRequest request) {
        try {
            District district = districtService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    district,
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
    @Operation(summary = "4. Get district by ID", description = "Returns a single district by its ID")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer districtId) {
        try {
            District district = districtService.edit(districtId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    district,
                    "District fetch successful",
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
    @Operation(summary = "5. Update a district", description = "Update an existing district by its ID.")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer districtId, @Valid @RequestBody DistrictRequest request) {
        try {
            District district = districtService.update(request, districtId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    district,
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
    @Operation(summary = "6. Delete a district", description = "Delete a district by its ID.")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer districtId) {
        try {
            districtService.destroy(districtId);

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
