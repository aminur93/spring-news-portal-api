package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.NewsImages;
import aminurdev.com.backend.domain.entity.NewsVideo;
import aminurdev.com.backend.domain.request.NewsVideosRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.NewsVideoService;
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
@RequestMapping("/api/v1/admin/news-videos")
@RequiredArgsConstructor
@Tag(
        name = "News Videos Api",
        description = "APIs for managing news videos, including CRUD operations for adding, updating, retrieving, and deleting video entries."
)
public class NewsVideosController {

    private final NewsVideoService newsVideoService;

    @Operation(
            summary = "Retrieve paginated list of news videos",
            description = "Fetches a paginated list of news videos sorted by the specified direction. Allows for customization of page number and the number of videos per page."
    )
    @GetMapping
    public ResponseEntity<PaginationResponse<NewsVideo>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<NewsVideo> paginationResponse = newsVideoService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @Operation(
            summary = "Retrieve all news videos",
            description = "Fetches a list of all news videos available in the system. This endpoint returns a comprehensive collection of video entries without pagination."
    )
    @GetMapping("/all")
    public ResponseEntity<GlobalResponse> getAllNewsVideos()
    {
        List<NewsVideo> newsVideos = newsVideoService.getAllNewsVideos();

        GlobalResponse response = new GlobalResponse().success(
                newsVideos,
                "Fetch successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Add a new news video",
            description = "Creates a new entry for a news video in the system. This endpoint accepts the necessary video details and stores them in the database. The response will include the created video object."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalResponse> store(@Valid @ModelAttribute NewsVideosRequest request)
    {
        try {

            NewsVideo newsVideo = newsVideoService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    newsVideo,
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
            summary = "Retrieve details of a specific news video",
            description = "Fetches the details of a news video by its ID. This endpoint allows users to retrieve the information of a specific video for viewing or editing purposes."
    )
    @GetMapping("{id}")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer newsVideoId)
    {
        try {

            NewsVideo newsVideo = newsVideoService.edit(newsVideoId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    newsVideo,
                    "Fetch news-video by id successful",
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
            summary = "Update an existing news video",
            description = "Updates the details of a news video identified by its ID. This endpoint accepts the new video information and modifies the existing entry in the database. The response will include the updated video object."
    )
    @PutMapping(value="{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer newsVideoId, @Valid @ModelAttribute NewsVideosRequest request)
    {
        try {

            NewsVideo newsVideo = newsVideoService.update(newsVideoId, request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    newsVideo,
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
            summary = "Delete a news video",
            description = "Removes a news video from the system based on its ID. This endpoint allows users to delete a specific video entry from the database. The response will confirm the deletion."
    )
    @DeleteMapping("{id}")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer newsVideoId)
    {
        try {

            newsVideoService.destroy(newsVideoId);

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
