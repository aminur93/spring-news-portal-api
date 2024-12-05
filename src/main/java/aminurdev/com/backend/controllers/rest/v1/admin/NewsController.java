package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.News;
import aminurdev.com.backend.domain.entity.NewsImages;
import aminurdev.com.backend.domain.request.NewsRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.NewsService;
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

import java.security.PublicKey;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/api/v1/admin/news")
@RequiredArgsConstructor
@Tag(name = "News Api",
        description = "APIs for managing news articles. This includes creating, reading, updating, and deleting news articles.")
public class NewsController {

    private final NewsService newsService;

    @Operation(
            summary = "Retrieve a list of news articles",
            description = "Fetches a paginated list of news articles. Users can specify parameters such as page number and size to control the output."
    )
    @GetMapping
    public ResponseEntity<PaginationResponse<News>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<News> paginationResponse = newsService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @Operation(
            summary = "Retrieve all news articles",
            description = "Fetches all news articles from the database. This endpoint returns a complete list of news items without pagination."
    )
    @GetMapping("/all")
    public ResponseEntity<GlobalResponse> getAllNews()
    {
        List<News> news = newsService.getAllNews();

        GlobalResponse response = new GlobalResponse().success(
                news,
                "Fetch successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Create a new news article",
            description = "Submits a new news article to the database. The request body should contain the details of the news article, such as title, content, and other relevant fields."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalResponse> store(@Valid @ModelAttribute NewsRequest request)
    {
        try {

            News news = newsService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    news,
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
            summary = "Retrieve a news article by ID",
            description = "Fetches the details of a specific news article using its unique identifier. If the article is not found, a 404 error is returned."
    )
    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer newsId)
    {
        try {

            News news = newsService.edit(newsId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    news,
                    "Fetch news by id successful",
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
            summary = "Update an existing news article",
            description = "Updates the details of an existing news article identified by its unique ID. The request body should contain the updated information for the news article. If the article is not found, a 404 error is returned."
    )
    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer newsId, NewsRequest request)
    {
        try {

            News news = newsService.update(newsId, request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    news,
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
            summary = "Increment the count for a specific news article",
            description = "Increments the count (e.g., views, likes) for the news article identified by its unique ID. This operation updates the relevant count and returns the updated news article data. If the article is not found, a 404 error is returned."
    )
    @PostMapping("/{id}/count")
    public ResponseEntity<GlobalResponse> countNews(@PathVariable Integer newsId) {

        News news = newsService.count(newsId);

        GlobalResponse globalResponse = new GlobalResponse().success(
                news,
                "View Count Update successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);
    }

    @Operation(
            summary = "Delete a news article by ID",
            description = "Deletes the news article identified by its unique ID from the database. If the article is successfully deleted, a 204 No Content response is returned. If the article is not found, a 404 error is returned."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable Integer newsId) {

        try {

            newsService.destroy(newsId);

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
