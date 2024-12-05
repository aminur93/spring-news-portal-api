package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.NewsVideo;
import aminurdev.com.backend.domain.request.NewsVideosRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface NewsVideoService {

    PaginationResponse<NewsVideo> index(Sort.Direction direction, int page, int perPage);

    List<NewsVideo> getAllNewsVideos();

    NewsVideo store(NewsVideosRequest request);

    NewsVideo edit(Integer newsVideoId);

    NewsVideo update(Integer newsVideoId, NewsVideosRequest request);

    void destroy(Integer newsVideoId);
}
