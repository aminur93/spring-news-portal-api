package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.Author;
import aminurdev.com.backend.domain.request.AuthorRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AuthorService {

    PaginationResponse<Author> index(Sort.Direction direction, int page, int perPage);

    List<Author> getAllAuthors();

    Author store(AuthorRequest request);

    Author edit(Integer authorId);

    Author update(Integer authorId, AuthorRequest request);

    void destroy(Integer authorId);
}
