package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.Country;
import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.CountryRepository;
import aminurdev.com.backend.domain.repository.UserRepository;
import aminurdev.com.backend.domain.request.CountryRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.CountryService;
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
public class CountryServiceImpl implements CountryService {


    private final CountryRepository countryRepository;

    private final UserRepository userRepository;

    @Override
    public PaginationResponse<Country> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<Country> countryPage = countryRepository.findAll(pageable);

        List<Country> countries = countryPage.getContent();

        PaginationResponse<Country> response = new PaginationResponse<>();

        response.setData(countries);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All countries fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(countryPage.getNumber() + 1);
        meta.setFrom(countryPage.getNumber() * countryPage.getSize() + 1);
        meta.setLastPage(countryPage.getTotalPages());
        meta.setPath("/country");
        meta.setPerPage(countryPage.getSize());
        meta.setTo((int) countryPage.getTotalElements());
        meta.setTotal((int) countryPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/country?page=1");
        links.setLast("/country?page=" + countryPage.getTotalPages());
        if (countryPage.hasPrevious()) {
            links.setPrev("/country?page=" + countryPage.previousPageable().getPageNumber());
        }
        if (countryPage.hasNext()) {
            links.setNext("/country?page=" + countryPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);
        return response;
    }

    @Override
    public List<Country> getAllCountries() {

        return countryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Country store(CountryRequest request) {

        try {
            User user = userRepository.findById(request.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getCreatedBy()));


            Country country = Country.builder()
                    .name_en(request.getName_en())
                    .name_bn(request.getName_bn())
                    .status(request.getStatus() != null ? request.getStatus() : false)
                    .createdBy(user.getId())
                    .build();

            return countryRepository.save(country);

        } catch (Exception exception) {

            throw new GlobalException("Error while storing country: " + exception.getMessage(), exception);
        }
    }

    @Override
    public Country edit(Integer countryId) {

        return countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country id is not found: " + countryId));
    }

    @Override
    public Country update(CountryRequest request, Integer countryId) {

        try {
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new RuntimeException("Country id is not found: " + countryId));

            if (request.getName_en() != null) {
                country.setName_en(request.getName_en());
            }

            if (request.getName_bn() != null) {
                country.setName_bn(request.getName_bn());
            }


            if (request.getStatus() != null) {
                country.setStatus(request.getStatus());
            }

            return countryRepository.save(country);

        } catch (Exception exception) {

            throw new GlobalException("Error while updating country: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer countryId) {

        try {
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new RuntimeException("Country id is not found: " + countryId));

            if (country.getId() != null) {
                // Delete all cities associated with the country
                countryRepository.deleteCitiesByCountryId(countryId);
            }

            countryRepository.delete(country);

        } catch (Exception exception) {
            throw new GlobalException("Error while deleting country: " + exception.getMessage(), exception);
        }
    }
}
