package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.NewsImagesGallery;
import aminurdev.com.backend.domain.request.NewsImagesGalleryRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.NewsImagesGalleryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/api/v1/admin/news-image-gallery")
@RequiredArgsConstructor
@Tag(name = "News Image Gallery Api", description = "API for managing the News Image Gallery, including uploading, updating, and deleting images.")
public class NewsImagesGalleryController {

    private final NewsImagesGalleryService newsImagesGalleryService;

    @Operation(
            summary = "Retrieve paginated list of News Image Galleries",
            description = "Fetches a paginated list of news image galleries, allowing sorting by direction and pagination parameters."
    )
    @GetMapping
    public ResponseEntity<PaginationResponse<NewsImagesGallery>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<NewsImagesGallery> paginationResponse = newsImagesGalleryService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @Operation(
            summary = "Retrieve all News Image Galleries",
            description = "Fetches a complete list of all news image galleries available in the system."
    )
    @GetMapping("/all")
    public ResponseEntity<GlobalResponse> getAllNewsImagesGallery()
    {
        List<NewsImagesGallery> newsImagesGalleries = newsImagesGalleryService.getAllNewsImagesGallery();

        GlobalResponse response = new GlobalResponse().success(
                newsImagesGalleries,
                "Fetch successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Create a new News Image Gallery",
            description = "Creates a new news image gallery entry with the provided details, including titles and associated images."
    )
    @PostMapping
    public ResponseEntity<GlobalResponse> store(@Valid @ModelAttribute NewsImagesGalleryRequest request)
    {

        try {

            NewsImagesGallery newsImagesGallery = newsImagesGalleryService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    newsImagesGallery,
                    "Store successful",
                    true,
                    HttpStatus.CREATED.value()
            );

            return ResponseEntity.status(HttpStatus.CREATED.value()).body(globalResponse);

        } catch (RequestRejectedException exception){

            GlobalResponse globalResponse = new GlobalResponse().error(
                    Collections.singletonList(exception.getMessage()),
                    "Failed server error",
                    false,
                    HttpStatus.NOT_FOUND.value()
            );

            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(globalResponse);

        } catch (Exception e) {

            GlobalResponse globalResponse = new GlobalResponse().error(
                    Collections.singletonList(e.getMessage()),
                    "Failed server error",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.ok(globalResponse);
        }
    }

    @Operation(
            summary = "Retrieve a specific News Image Gallery",
            description = "Fetches the details of a news image gallery by its ID, allowing users to view and edit the gallery information."
    )
    @GetMapping("{id}")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer newsImageGalleryId)
    {
        try {

            NewsImagesGallery newsImagesGallery = newsImagesGalleryService.edit(newsImageGalleryId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    newsImagesGallery,
                    "Fetch by id successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);

        }catch (RequestRejectedException exception){

            GlobalResponse globalResponse = new GlobalResponse().error(
                    Collections.singletonList(exception.getMessage()),
                    "Failed server error",
                    false,
                    HttpStatus.NOT_FOUND.value()
            );

            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(globalResponse);

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

    @Operation(
            summary = "Update an existing News Image Gallery",
            description = "Updates the details of a news image gallery identified by its ID with the provided information, including titles and associated images."
    )
    @PutMapping(value="{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer newsImageGalleryId, @Valid @ModelAttribute NewsImagesGalleryRequest request)
    {
        try {

            NewsImagesGallery newsImagesGallery = newsImagesGalleryService.update(newsImageGalleryId, request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    newsImagesGallery,
                    "Update successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);

        }catch (RequestRejectedException exception){

            GlobalResponse globalResponse = new GlobalResponse().error(
                    Collections.singletonList(exception.getMessage()),
                    "Failed server error",
                    false,
                    HttpStatus.NOT_FOUND.value()
            );

            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(globalResponse);

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

    @Operation(
            summary = "Delete a News Image Gallery",
            description = "Deletes a news image gallery identified by its ID from the system. This action cannot be undone."
    )
    @DeleteMapping("{id}")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer newsImageGalleryId)
    {
        try {

            newsImagesGalleryService.destroy(newsImageGalleryId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    "",
                    "Delete successful",
                    true,
                    HttpStatus.OK.value()
            );

            return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);

        }catch (RequestRejectedException exception){

            GlobalResponse globalResponse = new GlobalResponse().error(
                    Collections.singletonList(exception.getMessage()),
                    "Failed server error",
                    false,
                    HttpStatus.NOT_FOUND.value()
            );

            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(globalResponse);

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
