package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.Comment;
import aminurdev.com.backend.domain.request.CommentRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CommentService {

    PaginationResponse<Comment> index(Sort.Direction direction, int page, int perPage);

    List<Comment> getAllComments();

    Comment store(CommentRequest request);

    Comment edit(Integer commentId);

    Comment update(Integer commentId, CommentRequest request);

    void destroy(Integer commentId);
}
