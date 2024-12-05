package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.District;
import aminurdev.com.backend.domain.request.DistrictRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface DistrictService {

    PaginationResponse<District> index(Sort.Direction direction, int page, int perPage);

    List<District> getAllDistricts();

    District store(DistrictRequest request);

    District edit(Integer districtId);

    District update(DistrictRequest request, Integer districtId);

    void destroy(Integer districtId);
}
