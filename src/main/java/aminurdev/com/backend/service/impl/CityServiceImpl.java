package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.City;
import aminurdev.com.backend.domain.entity.Country;
import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.CityRepository;
import aminurdev.com.backend.domain.repository.CountryRepository;
import aminurdev.com.backend.domain.repository.UserRepository;
import aminurdev.com.backend.domain.request.CityRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;

    @Override
    public PaginationResponse<City> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<City> cityPage = cityRepository.findAll(pageable);
        List<City> cities = cityPage.getContent();

        PaginationResponse<City> response = new PaginationResponse<>();
        response.setData(cities);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All cities fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(cityPage.getNumber() + 1);
        meta.setFrom(cityPage.getNumber() * cityPage.getSize() + 1);
        meta.setLastPage(cityPage.getTotalPages());
        meta.setPath("/city");
        meta.setPerPage(cityPage.getSize());
        meta.setTo((int) cityPage.getTotalElements());
        meta.setTotal((int) cityPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/city?page=1");
        links.setLast("/city?page=" + cityPage.getTotalPages());
        if (cityPage.hasPrevious()) {
            links.setPrev("/city?page=" + cityPage.previousPageable().getPageNumber());
        }
        if (cityPage.hasNext()) {
            links.setNext("/city?page=" + cityPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);
        return response;
    }

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public City store(CityRequest request) {
        try {
            Country country = countryRepository.findById(request.getCountryId())
                    .orElseThrow(() -> new RuntimeException("Country not found with ID: " + request.getCountryId()));
            User user = userRepository.findById(request.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getCreatedBy()));

            City city = City.builder()
                    .name_en(request.getName_en())
                    .name_bn(request.getName_bn())
                    .status(request.getStatus() != null ? request.getStatus() : false)
                    .country(country)
                    .createdBy(user.getId())
                    .build();

            return cityRepository.save(city);

        } catch (Exception exception) {
            throw new GlobalException("Error while storing city: " + exception.getMessage(), exception);
        }
    }

    @Override
    public City edit(Integer cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City id is not found: " + cityId));
    }

    @Override
    public City update(CityRequest request, Integer cityId) {
        try {
            City city = cityRepository.findById(cityId)
                    .orElseThrow(() -> new RuntimeException("City id is not found: " + cityId));

            if (request.getName_en() != null) {
                city.setName_en(request.getName_en());
            }

            if (request.getName_bn() != null) {
                city.setName_bn(request.getName_bn());
            }

            if (request.getCountryId() != null) {
                Country country = countryRepository.findById(request.getCountryId())
                        .orElseThrow(() -> new RuntimeException("Country not found with ID: " + request.getCountryId()));
                city.setCountry(country);
            }


            if (request.getStatus() != null) {
                city.setStatus(request.getStatus());
            }

            return cityRepository.save(city);

        } catch (Exception exception) {
            throw new GlobalException("Error while updating city: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer cityId) {
        try {
            City city = cityRepository.findById(cityId)
                    .orElseThrow(() -> new RuntimeException("City id is not found: " + cityId));

            if (city.getId() != null) {
                // Delete all divisions associated with the city
                cityRepository.deleteDivisionsByCityId(cityId);
            }

            cityRepository.delete(city);

        } catch (Exception exception) {
            throw new GlobalException("Error while deleting city: " + exception.getMessage(), exception);
        }
    }
}
