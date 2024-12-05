package aminurdev.com.backend.service.impl;


import aminurdev.com.backend.domain.entity.City;
import aminurdev.com.backend.domain.entity.Division;
import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.CityRepository;
import aminurdev.com.backend.domain.repository.DivisionRepository;
import aminurdev.com.backend.domain.repository.UserRepository;
import aminurdev.com.backend.domain.request.DivisionRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.DivisionService;
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
public class DivisionServiceImpl implements DivisionService {
    private final DivisionRepository divisionRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;

    @Override
    public PaginationResponse<Division> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<Division> divisionPage = divisionRepository.findAll(pageable);
        List<Division> divisions = divisionPage.getContent();

        PaginationResponse<Division> response = new PaginationResponse<>();
        response.setData(divisions);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All divisions fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(divisionPage.getNumber() + 1);
        meta.setFrom(divisionPage.getNumber() * divisionPage.getSize() + 1);
        meta.setLastPage(divisionPage.getTotalPages());
        meta.setPath("/division");
        meta.setPerPage(divisionPage.getSize());
        meta.setTo((int) divisionPage.getTotalElements());
        meta.setTotal((int) divisionPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/division?page=1");
        links.setLast("/division?page=" + divisionPage.getTotalPages());
        if (divisionPage.hasPrevious()) {
            links.setPrev("/division?page=" + divisionPage.previousPageable().getPageNumber());
        }
        if (divisionPage.hasNext()) {
            links.setNext("/division?page=" + divisionPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);
        return response;
    }

    @Override
    public List<Division> getAllDivisions() {
        return divisionRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Division store(DivisionRequest request) {
        try {
            City city = cityRepository.findById(request.getCityId())
                    .orElseThrow(() -> new RuntimeException("City not found with ID: " + request.getCityId()));
            User user = userRepository.findById(request.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getCreatedBy()));

            Division division = Division.builder()
                    .name_en(request.getName_en())
                    .name_bn(request.getName_bn())
                    .status(request.getStatus() != null ? request.getStatus() : false)
                    .city(city)
                    .createdBy(user.getId())
                    .build();

            return divisionRepository.save(division);

        } catch (Exception exception) {
            throw new GlobalException("Error while storing division: " + exception.getMessage(), exception);
        }
    }

    @Override
    public Division edit(Integer divisionId) {
        return divisionRepository.findById(divisionId)
                .orElseThrow(() -> new RuntimeException("Division id is not found: " + divisionId));
    }

    @Override
    public Division update(DivisionRequest request, Integer divisionId) {
        try {
            Division division = divisionRepository.findById(divisionId)
                    .orElseThrow(() -> new RuntimeException("Division id is not found: " + divisionId));

            if (request.getName_en() != null) {
                division.setName_en(request.getName_en());
            }

            if (request.getName_bn() != null) {
                division.setName_bn(request.getName_bn());
            }

            if (request.getCityId() != null) {
                City city = cityRepository.findById(request.getCityId())
                        .orElseThrow(() -> new RuntimeException("City not found with ID: " + request.getCityId()));
                division.setCity(city);
            }

            if (request.getStatus() != null) {
                division.setStatus(request.getStatus());
            }

            return divisionRepository.save(division);

        } catch (Exception exception) {
            throw new GlobalException("Error while updating division: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer divisionId) {
        try {
            Division division = divisionRepository.findById(divisionId)
                    .orElseThrow(() -> new RuntimeException("Division id is not found: " + divisionId));

            if (division.getId() != null) {
                // Delete all districts associated with the division
                divisionRepository.deleteDistrictsByDivisionId(divisionId);
            }

            divisionRepository.delete(division);

        } catch (Exception exception) {
            throw new GlobalException("Error while deleting division: " + exception.getMessage(), exception);
        }
    }
}
