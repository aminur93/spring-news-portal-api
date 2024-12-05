package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.City;
import aminurdev.com.backend.domain.request.CityRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.CityService;
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
@RequestMapping("/api/v1/admin/city")
@RequiredArgsConstructor
@Tag(name = "City Api", description = "Operations related to managing cities")
public class CityController {

    private final CityService cityService;

    @GetMapping
    @Operation(summary = "1. Get all cities with pagination", description = "Retrieve a list of all cities with pagination.")
    public ResponseEntity<PaginationResponse<City>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<City> paginationResponse = cityService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all-cities")
    @Operation(summary = "2. Get all cities", description = "Retrieve a list of all cities without pagination.")
    public ResponseEntity<GlobalResponse> getAllCities() {
        List<City> cities = cityService.getAllCities();

        GlobalResponse globalResponse = new GlobalResponse().success(
                cities,
                "Fetch all cities successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);
    }

    @PostMapping
    @Operation(summary = "3. Store a city", description = "Create a new city")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody CityRequest request) {
        try {
            City city = cityService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    city,
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
    @Operation(summary = "4. Get city by ID", description = "Returns a single city by its ID")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer cityId) {
        try {
            City city = cityService.edit(cityId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    city,
                    "City fetch successful",
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
    @Operation(summary = "5. Update a city", description = "Update an existing city by its ID.")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer cityId, @Valid @RequestBody CityRequest request) {
        try {
            City city = cityService.update(request, cityId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    city,
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
    @Operation(summary = "6. Delete a city", description = "Delete a city by its ID.")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer cityId) {
        try {
            cityService.destroy(cityId);

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
