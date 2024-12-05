package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.Country;
import aminurdev.com.backend.domain.request.CountryRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.CountryService;
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
@RequestMapping("/api/v1/admin/country")
@RequiredArgsConstructor
@Tag(name = "Country Api", description = "Operations related to managing countries")

public class CountryController {
    private final CountryService countryService;

    @GetMapping
    @Operation(summary = "1. Get all countries with pagination", description = "Retrieve a list of all countries with pagination.")
    public ResponseEntity<PaginationResponse<Country>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Country> paginationResponse = countryService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all-countries")
    @Operation(summary = "2. Get all countries", description = "Retrieve a list of all countries without pagination.")
    public ResponseEntity<GlobalResponse> getAllCountries() {
        List<Country> countries = countryService.getAllCountries();

        GlobalResponse globalResponse = new GlobalResponse().success(
                countries,
                "Fetch all countries successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);
    }

    @PostMapping
    @Operation(summary = "3. Store a country", description = "Create a new country")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody CountryRequest request) {
        try {
            Country country = countryService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    country,
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
    @Operation(summary = "4. Get country by ID", description = "Returns a single country by its ID")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer countryId) {
        try {
            Country country = countryService.edit(countryId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    country,
                    "Country fetch successful",
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
    @Operation(summary = "5. Update a country", description = "Update an existing country by its ID.")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer countryId, @Valid @RequestBody CountryRequest request) {
        try {
            Country country = countryService.update(request, countryId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    country,
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
    @Operation(summary = "6. Delete a country", description = "Delete a country by its ID.")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer countryId) {
        try {
            countryService.destroy(countryId);

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
