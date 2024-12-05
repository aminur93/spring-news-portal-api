package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.NewsVideoGallery;
import aminurdev.com.backend.domain.request.NewsVideoGalleryRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.NewsVideoGalleryService;
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
@RequestMapping("/api/v1/admin/news-video-gallery")
@RequiredArgsConstructor
@Tag(
        name = "News Video Gallery Api",
        description = "APIs for managing the News Video Gallery, including CRUD operations for video entries. This includes creating, reading, updating, and deleting videos related to news items. The APIs support various functionalities such as uploading videos, retrieving video details, and managing video metadata."
)
public class NewsVideoGalleryController {

    private final NewsVideoGalleryService newsVideoGalleryService;

    @Operation(
            summary = "Retrieve paginated list of News Videos Galleries",
            description = "Fetches a paginated list of news video galleries, allowing sorting by direction and pagination parameters."
    )
    @GetMapping
    public ResponseEntity<PaginationResponse<NewsVideoGallery>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){


        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<NewsVideoGallery> paginationResponse = newsVideoGalleryService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @Operation(
            summary = "Retrieve all News Video Galleries",
            description = "Fetches a complete list of all news video galleries available in the system."
    )
    @GetMapping("/all")
    public ResponseEntity<GlobalResponse> getAllNewsVideoGallery()
    {
        List<NewsVideoGallery> newsVideoGalleries = newsVideoGalleryService.getAllNewsVideosGallery();

        GlobalResponse response = new GlobalResponse().success(
                newsVideoGalleries,
                "Fetch successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Create a new News Video Gallery",
            description = "Creates a new news video gallery entry with the provided details, including titles and associated images."
    )
    @PostMapping
    public ResponseEntity<GlobalResponse> store(@Valid @ModelAttribute NewsVideoGalleryRequest request)
    {

        try {

            NewsVideoGallery newsVideoGallery = newsVideoGalleryService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    newsVideoGallery,
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
            summary = "Retrieve a specific News video Gallery",
            description = "Fetches the details of a news video gallery by its ID, allowing users to view and edit the gallery information."
    )
    @GetMapping("{id}")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer newsVideoGalleryId)
    {
        try {

            NewsVideoGallery newsVideoGallery = newsVideoGalleryService.edit(newsVideoGalleryId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    newsVideoGallery,
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
            summary = "Update an existing News Video Gallery",
            description = "Updates the details of a news video gallery identified by its ID with the provided information, including titles and associated videos."
    )
    @PutMapping(value="{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer newsVideoGalleryId, @Valid @ModelAttribute NewsVideoGalleryRequest request)
    {
        try {

            NewsVideoGallery newsVideoGallery = newsVideoGalleryService.update(newsVideoGalleryId, request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    newsVideoGallery,
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
            summary = "Delete a News Video Gallery",
            description = "Deletes a news video gallery identified by its ID from the system. This action cannot be undone."
    )
    @DeleteMapping("{id}")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer newsVideoGalleryId)
    {
        try {

            newsVideoGalleryService.destroy(newsVideoGalleryId);

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
