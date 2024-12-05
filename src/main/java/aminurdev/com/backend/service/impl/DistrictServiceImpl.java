package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.District;
import aminurdev.com.backend.domain.entity.Division;
import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.DistrictRepository;
import aminurdev.com.backend.domain.repository.DivisionRepository;
import aminurdev.com.backend.domain.repository.UserRepository;
import aminurdev.com.backend.domain.request.DistrictRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.DistrictService;
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
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;
    private final DivisionRepository divisionRepository;
    private final UserRepository userRepository;

    @Override
    public PaginationResponse<District> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<District> districtPage = districtRepository.findAll(pageable);
        List<District> districts = districtPage.getContent();

        PaginationResponse<District> response = new PaginationResponse<>();
        response.setData(districts);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All districts fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(districtPage.getNumber() + 1);
        meta.setFrom(districtPage.getNumber() * districtPage.getSize() + 1);
        meta.setLastPage(districtPage.getTotalPages());
        meta.setPath("/district");
        meta.setPerPage(districtPage.getSize());
        meta.setTo((int) districtPage.getTotalElements());
        meta.setTotal((int) districtPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/district?page=1");
        links.setLast("/district?page=" + districtPage.getTotalPages());
        if (districtPage.hasPrevious()) {
            links.setPrev("/district?page=" + districtPage.previousPageable().getPageNumber());
        }
        if (districtPage.hasNext()) {
            links.setNext("/district?page=" + districtPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);
        return response;
    }

    @Override
    public List<District> getAllDistricts() {
        return districtRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public District store(DistrictRequest request) {
        try {
            Division division = divisionRepository.findById(request.getDivisionId())
                    .orElseThrow(() -> new RuntimeException("Division not found with ID: " + request.getDivisionId()));
            User user = userRepository.findById(request.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getCreatedBy()));

            District district = District.builder()
                    .name_en(request.getName_en())
                    .name_bn(request.getName_bn())
                    .status(request.getStatus() != null ? request.getStatus() : false)
                    .division(division)
                    .createdBy(user.getId())
                    .build();

            return districtRepository.save(district);

        } catch (Exception exception) {
            throw new GlobalException("Error while storing district: " + exception.getMessage(), exception);
        }
    }

    @Override
    public District edit(Integer districtId) {
        return districtRepository.findById(districtId)
                .orElseThrow(() -> new RuntimeException("District id is not found: " + districtId));
    }

    @Override
    public District update(DistrictRequest request, Integer districtId) {
        try {
            District district = districtRepository.findById(districtId)
                    .orElseThrow(() -> new RuntimeException("District id is not found: " + districtId));

            if (request.getName_en() != null) {
                district.setName_en(request.getName_en());
            }

            if (request.getName_bn() != null) {
                district.setName_bn(request.getName_bn());
            }

            if (request.getDivisionId() != null) {
                Division division = divisionRepository.findById(request.getDivisionId())
                        .orElseThrow(() -> new RuntimeException("City not found with ID: " + request.getDivisionId()));
                district.setDivision(division);
            }

            if (request.getStatus() != null) {
                district.setStatus(request.getStatus());
            }

            return districtRepository.save(district);

        } catch (Exception exception) {
            throw new GlobalException("Error while updating district: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer districtId) {
        try {
            District district = districtRepository.findById(districtId)
                    .orElseThrow(() -> new RuntimeException("District id is not found: " + districtId));

            if (district.getId() != null) {
                // Delete all zillas associated with the district
                districtRepository.deleteZillasByDistrictId(districtId);
            }

            districtRepository.delete(district);

        } catch (Exception exception) {
            throw new GlobalException("Error while deleting district: " + exception.getMessage(), exception);
        }
    }
}
