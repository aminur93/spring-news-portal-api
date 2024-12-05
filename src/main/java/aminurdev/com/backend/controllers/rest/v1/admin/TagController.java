package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.TagEntity;
import aminurdev.com.backend.domain.request.TagRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.TagService;
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
@RequestMapping("/api/v1/admin/tag")
@RequiredArgsConstructor
@Tag(name = "Tag Api", description = "Operations related to managing tags")
public class TagController {

    private final TagService tagService;

    @GetMapping
    @Operation(summary = "1. Get all tags with pagination", description = "Retrieve a list of all tags with pagination.")
    public ResponseEntity<PaginationResponse<TagEntity>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<TagEntity> paginationResponse = tagService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all-tags")
    @Operation(summary = "2. Get all tags", description = "Retrieve a list of all tags without pagination.")
    public ResponseEntity<GlobalResponse> getAllTags() {
        List<TagEntity> tags = tagService.getAllTags();

        GlobalResponse globalResponse = new GlobalResponse().success(
                tags,
                "Fetch all tags successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);
    }

    @PostMapping
    @Operation(summary = "3. Store tag", description = "Create a new tag")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody TagRequest request) {
        try {
            TagEntity tag = tagService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    tag,
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
    @Operation(summary = "4. Get tag by ID", description = "Returns a single tag by its ID")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer tagId) {
        try {
            TagEntity tag = tagService.edit(tagId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    tag,
                    "TagEntity fetch successful",
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
    @Operation(summary = "5. Update tag", description = "Update an existing tag by its ID.")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer tagId, @Valid @RequestBody TagRequest request) {
        try {
            TagEntity tag = tagService.update(request, tagId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    tag,
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
    @Operation(summary = "6. Delete a tag", description = "Delete a tag by its ID.")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer tagId) {
        try {
            tagService.destroy(tagId);

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
