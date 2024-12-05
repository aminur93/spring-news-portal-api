package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.NewsLetter;
import aminurdev.com.backend.domain.request.NewsLetterRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface NewsLetterService {

    PaginationResponse<NewsLetter> index(Sort.Direction direction, int page, int perPage);

    List<NewsLetter> getAllNewsletter();

    NewsLetter store(NewsLetterRequest request);

    void destroy(Integer newsLetterId);
}
