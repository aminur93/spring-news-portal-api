package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.NewsImagesGallery;
import aminurdev.com.backend.domain.request.NewsImagesGalleryRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface NewsImagesGalleryService {

    PaginationResponse<NewsImagesGallery> index(Sort.Direction direction, int page, int perPage);

    List<NewsImagesGallery> getAllNewsImagesGallery();

    NewsImagesGallery store(NewsImagesGalleryRequest request);

    NewsImagesGallery edit(Integer newsImageGalleryId);

    NewsImagesGallery update(Integer newsImageGalleryId, NewsImagesGalleryRequest request);

    void destroy(Integer newsImageGalleryId);
}
