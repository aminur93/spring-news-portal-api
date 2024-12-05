package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.Advertisement;
import aminurdev.com.backend.domain.request.AdvertisementRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AdvertisementService {

    PaginationResponse<Advertisement> index(Sort.Direction direction, int page, int perPage);

    List<Advertisement> getAllAdvertisements();

    Advertisement store(AdvertisementRequest request);

    Advertisement edit(Integer advertisementId);

    Advertisement update(AdvertisementRequest request, Integer advertisementId);

    void destroy(Integer advertisementId);
}
