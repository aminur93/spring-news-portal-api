package aminurdev.com.backend.controllers.rest.v1.front;

import aminurdev.com.backend.domain.entity.Category;
import aminurdev.com.backend.domain.entity.Comment;
import aminurdev.com.backend.domain.entity.NewsImages;
import aminurdev.com.backend.domain.entity.NewsLetter;
import aminurdev.com.backend.domain.request.CommentRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.CommentService;
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
@RequiredArgsConstructor
@Tag(name = "Comment API", description = "CRUD API for managing comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/v1/admin/comment")
    public ResponseEntity<PaginationResponse<Comment>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Comment> paginationResponse = commentService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/api/v1/admin/comment/all")
    public ResponseEntity<GlobalResponse> getAllComments()
    {
        List<Comment> comments = commentService.getAllComments();

        GlobalResponse globalResponse = new GlobalResponse().success(
                comments,
                "Fetch all comments successful",
                true,
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(globalResponse);
    }

    @PostMapping(value = "/api/v1/public/comment", consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalResponse> store(@Valid @ModelAttribute CommentRequest request)
    {
        try {

            Comment comment = commentService.store(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    comment,
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

    @GetMapping("/api/v1/admin/comment/{id}")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Integer commentId)
    {
        try {

            Comment comment = commentService.edit(commentId);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    comment,
                    "Fetch comment by id successful",
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

    @PutMapping(value = "/api/v1/admin/comment/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Integer commentId, @Valid @ModelAttribute CommentRequest request)
    {
        try {

            Comment comment = commentService.update(commentId, request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    comment,
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

    @DeleteMapping("api/v1/admin/comment/{id}")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Integer commentId)
    {
        try {

            commentService.destroy(commentId);

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
