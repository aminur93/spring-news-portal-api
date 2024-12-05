package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.Advertisement;
import aminurdev.com.backend.domain.request.AdvertisementRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.AdvertisementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/api/v1/admin/advertisement")
@RequiredArgsConstructor
@Tag(name = "Advertisement Api", description = "Operations related to managing advertisements")

public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @GetMapping
    @Operation(summary = "1. Get all advertisements with pagination", description = "Retrieve a list of all advertisements with pagination.")
    public ResponseEntity<PaginationResponse<Advertisement>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Advertisement> paginationResponse = advertisementService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all-advertisements")
    @Operation(summary = "2. Get all advertisements", description = "Retrieve a list of all advertisements without pagination.")
    public ResponseEntity<GlobalResponse> getAllAdvertisements() {
        List<Advertisement> advertisements = advertisementService.getAllAdvertisements();

        GlobalResponse globalResponse = new GlobalResponse().success(
                advertisements,
                "Fetch all advertisements successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Store advertisement",
            description = "Endpoint to upload advertisement details with an image file"
    )
    @ApiResponse(responseCode = "200", description = "Image uploaded successfully")
    public ResponseEntity<GlobalResponse> store(@Valid @ModelAttribute AdvertisementRequest request) {
        try {
            Advertisement advertisement = advertisementService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    advertisement,
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
    @Operation(summary = "4. Get advertisement by ID", description = "Returns a single advertisement by its ID")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer advertisementId) {
        try {
            Advertisement advertisement = advertisementService.edit(advertisementId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    advertisement,
                    "Advertisement fetch successful",
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

    @PutMapping(value="{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "5. Update an advertisement", description = "Update an existing advertisement by its ID.")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer advertisementId, @Valid @ModelAttribute AdvertisementRequest request) {
        try {
            Advertisement advertisement = advertisementService.update(request, advertisementId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    advertisement,
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
    @Operation(summary = "6. Delete an advertisement", description = "Delete an advertisement by its ID.")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer advertisementId) {
        try {
            advertisementService.destroy(advertisementId);

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
