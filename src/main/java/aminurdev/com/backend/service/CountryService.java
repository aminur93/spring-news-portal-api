package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.Country;
import aminurdev.com.backend.domain.request.CountryRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;


public interface CountryService {

    PaginationResponse<Country> index(Sort.Direction direction, int page, int perPage);

    List<Country> getAllCountries();

    Country store(CountryRequest request);

    Country edit(Integer countryId);

    Country update(CountryRequest request, Integer countryId);

    void destroy(Integer countryId);
}
