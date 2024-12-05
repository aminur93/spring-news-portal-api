package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.City;
import aminurdev.com.backend.domain.request.CityRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CityService {

    PaginationResponse<City> index(Sort.Direction direction, int page, int perPage);

    List<City> getAllCities();

    City store(CityRequest request);

    City edit(Integer cityId);

    City update(CityRequest request, Integer cityId);

    void destroy(Integer cityId);
}
