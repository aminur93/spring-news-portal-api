package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.NewsImages;
import aminurdev.com.backend.domain.request.NewsImagesRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface NewsImagesService {

    PaginationResponse<NewsImages> index(Sort.Direction direction, int page, int perPage);

    List<NewsImages> getAllNewsImages();

    NewsImages store(NewsImagesRequest request);

    NewsImages edit(Integer newsImageId);

    NewsImages update(Integer newsImageId, NewsImagesRequest request);

    void destroy(Integer newsImageId);
}
