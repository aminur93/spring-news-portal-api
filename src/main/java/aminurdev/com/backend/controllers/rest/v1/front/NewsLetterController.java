package aminurdev.com.backend.controllers.rest.v1.front;

import aminurdev.com.backend.domain.entity.NewsLetter;
import aminurdev.com.backend.domain.request.NewsLetterRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.NewsLetterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequiredArgsConstructor
@Tag(name = "Newsletter API", description = "Operations related to managing newsletter subscriptions, including creating, viewing, and deleting subscriptions.")
public class NewsLetterController {

    private final NewsLetterService newsLetterService;

    @Operation(
            summary = "Retrieve a paginated list of newsletters",
            description = "Fetches a list of newsletters with pagination support. Allows sorting by direction (ASC/DESC), and customizes the page number and items per page."
    )
    @GetMapping("/api/v1/admin/news-letter")
    public ResponseEntity<PaginationResponse<NewsLetter>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<NewsLetter> paginationResponse = newsLetterService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @Operation(
            summary = "Create a new newsletter subscription",
            description = "Allows users to subscribe to the newsletter by submitting their email and other necessary details. Stores the subscription information in the database."
    )
    @PostMapping("/api/v1/public/news-letter")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody NewsLetterRequest request)
    {
        try {

            NewsLetter newsLetter = newsLetterService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    newsLetter,
                    "Subscribe successful",
                    true,
                    HttpStatus.CREATED.value()
            );

            return ResponseEntity.status(HttpStatus.CREATED.value()).body(globalResponse);

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

            return ResponseEntity.ok(globalResponse);
        }
    }

    @Operation(
            summary = "Delete a newsletter subscription",
            description = "Removes a newsletter subscription from the system by its ID. This action is permanent and cannot be undone."
    )
    @DeleteMapping("api/v1/admin/news-letter/{id}")
    public ResponseEntity<GlobalResponse> destroy(Integer newsLetterId)
    {
        try {

            newsLetterService.destroy(newsLetterId);

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
