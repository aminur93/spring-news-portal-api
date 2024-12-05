package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.UpZilla;
import aminurdev.com.backend.domain.request.UpZillaRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.UpZillaService;
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
@RequestMapping("/api/v1/admin/upzilla")
@RequiredArgsConstructor
@Tag(name = "UpZilla Api", description = "Operations related to managing upzillas")
public class UpZillaController {

    private final UpZillaService upZillaService;

    @GetMapping
    @Operation(summary = "1. Get all upzillas with pagination", description = "Retrieve a list of all upzillas with pagination.")
    public ResponseEntity<PaginationResponse<UpZilla>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<UpZilla> paginationResponse = upZillaService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all-upzillas")
    @Operation(summary = "2. Get all upzillas", description = "Retrieve a list of all upzillas without pagination.")
    public ResponseEntity<GlobalResponse> getAllUpZillas() {
        List<UpZilla> upZillas = upZillaService.getAllUpZillas();

        GlobalResponse globalResponse = new GlobalResponse().success(
                upZillas,
                "Fetch all upzillas successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);
    }

    @PostMapping
    @Operation(summary = "3. Store an upzilla", description = "Create a new upzilla")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody UpZillaRequest request) {
        try {
            UpZilla upZilla = upZillaService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    upZilla,
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
    @Operation(summary = "4. Get upzilla by ID", description = "Returns a single upzilla by its ID")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer upzillaId) {
        try {
            UpZilla upZilla = upZillaService.edit(upzillaId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    upZilla,
                    "Upzilla fetch successful",
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
    @Operation(summary = "5. Update an upzilla", description = "Update an existing upzilla by its ID.")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer upzillaId, @Valid @RequestBody UpZillaRequest request) {
        try {
            UpZilla upZilla = upZillaService.update(request, upzillaId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    upZilla,
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
    @Operation(summary = "6. Delete an upzilla", description = "Delete an upzilla by its ID.")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer upzillaId) {
        try {
            upZillaService.destroy(upzillaId);

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
