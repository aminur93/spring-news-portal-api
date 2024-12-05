package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.NewsVideoGallery;
import aminurdev.com.backend.domain.request.NewsVideoGalleryRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface NewsVideoGalleryService {

    PaginationResponse<NewsVideoGallery> index(Sort.Direction direction, int page, int perPage);

    List<NewsVideoGallery> getAllNewsVideosGallery();

    NewsVideoGallery store(NewsVideoGalleryRequest request);

    NewsVideoGallery edit(Integer newsVideoGalleryId);

    NewsVideoGallery update(Integer newsVideoGalleryId, NewsVideoGalleryRequest request);

    void destroy(Integer newsVideoGalleryId);
}
