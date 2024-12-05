package aminurdev.com.backend.controllers.rest.v1.admin;

import aminurdev.com.backend.domain.entity.Advertisement;
import aminurdev.com.backend.domain.entity.Author;
import aminurdev.com.backend.domain.entity.Menu;
import aminurdev.com.backend.domain.request.AuthorRequest;
import aminurdev.com.backend.domain.request.MenuRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/api/v1/admin/author")
@RequiredArgsConstructor
@Tag(name = "Author Api", description = "Operations related to managing author")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    @Operation(summary = "1. Get all authors with pagination", description = "Retrieve a list of all authors with pagination.")
    public ResponseEntity<PaginationResponse<Author>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    )
    {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Author> paginationResponse = authorService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all-authors")
    @Operation(summary = "2. Get all authors", description = "Retrieve a list of all authors")
    public ResponseEntity<GlobalResponse> getAllAuthors()
    {
        List<Author> authors = authorService.getAllAuthors();

        GlobalResponse globalResponse = new GlobalResponse().success(
                authors,
                "Get all authors fetch successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(globalResponse);
    }

    @PostMapping
    @Operation(summary = "3. Store author", description = "Create a new author")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody AuthorRequest request)
    {
        try {

            Author author = authorService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    author,
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

    @GetMapping("{id}")
    @Operation(summary = "Edit an existing author", description = "Updates an existing author item based on the provided ID and new details.")
    public ResponseEntity<GlobalResponse> edit(Integer authorId)
    {
        try {

            Author author = authorService.edit(authorId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    author,
                    "Fetch author by id successful",
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

    @PutMapping("{id}")
    @Operation(
            summary = "Update an existing author",
            description = "Updates an existing author item based on the provided ID and the new details."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer authorId, @Valid @RequestBody AuthorRequest request)
    {
        try {

            Author author = authorService.update(authorId, request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    author,
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

    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete an existing author",
            description = "Deletes an existing author item based on the provided ID."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer authorId)
    {
        try {

            authorService.destroy(authorId);

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
