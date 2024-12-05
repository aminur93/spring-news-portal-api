package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.Author;
import aminurdev.com.backend.domain.entity.NewsImages;
import aminurdev.com.backend.domain.entity.Permission;
import aminurdev.com.backend.domain.request.NewsImagesRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.NewsImagesService;
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
@RequestMapping("/api/v1/admin/news-image")
@RequiredArgsConstructor
@Tag(name = "News Image API", description = "CRUD API for managing News Image")
public class NewsImageController {

    private final NewsImagesService newsImagesService;

    @Operation(
            summary = "Fetch paginated news images",
            description = "Retrieves a paginated list of news images with optional sorting direction. The default sorting direction is 'DESC', and pagination defaults to page 1 with 10 items per page."
    )
    @GetMapping
    public ResponseEntity<PaginationResponse<NewsImages>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<NewsImages> paginationResponse = newsImagesService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @Operation(
            summary = "Fetch all news images",
            description = "Retrieves a list of all news images without pagination. Returns a global response indicating success along with the list of news images."
    )
    @GetMapping("/all")
    public ResponseEntity<GlobalResponse> getAllNewsImages()
    {
        List<NewsImages> newsImages = newsImagesService.getAllNewsImages();

        GlobalResponse response = new GlobalResponse().success(
                newsImages,
                "Fetch successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Create a new news image",
            description = "Stores a new news image based on the provided request data. The request is validated before storing the image, and a global response is returned indicating the result of the operation."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalResponse> store(@Valid @ModelAttribute NewsImagesRequest request)
    {
        try {

            NewsImages newsImages = newsImagesService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    newsImages,
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
            summary = "Get news image details by ID",
            description = "Retrieves the details of a specific news image based on the provided ID. Returns a global response with the news image data if found."
    )
    @GetMapping("{id}")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer newsImageId)
    {
        try {

            NewsImages newsImages = newsImagesService.edit(newsImageId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    newsImages,
                    "Fetch news-images by id successful",
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
            summary = "Update news image by ID",
            description = "Updates an existing news image based on the provided ID and request data. The request data is validated and submitted as a multipart form. Returns a global response indicating the result of the update operation."
    )
    @PutMapping(value="{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer newsImageId, @Valid @ModelAttribute NewsImagesRequest request)
    {
        try {

            NewsImages newsImages = newsImagesService.update(newsImageId, request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    newsImages,
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
            summary = "Delete news image by ID",
            description = "Deletes a specific news image based on the provided ID. Returns a global response indicating the result of the deletion operation."
    )
    @DeleteMapping("{id}")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer newsImageId)
    {
        try {

            newsImagesService.destroy(newsImageId);

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
