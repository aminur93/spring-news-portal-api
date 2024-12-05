package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.News;
import aminurdev.com.backend.domain.request.NewsRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface NewsService {

    PaginationResponse<News> index(Sort.Direction direction, int page, int perPage);

    List<News> getAllNews();

    News store(NewsRequest request);

    News edit(Integer newsId);

    News update(Integer newsId, NewsRequest request);

    News count(Integer newsId);

    void destroy(Integer newsId);
}
