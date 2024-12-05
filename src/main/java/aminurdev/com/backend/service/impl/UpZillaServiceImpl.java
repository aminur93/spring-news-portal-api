package aminurdev.com.backend.service.impl;


import aminurdev.com.backend.domain.entity.District;
import aminurdev.com.backend.domain.entity.UpZilla;
import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.DistrictRepository;
import aminurdev.com.backend.domain.repository.UpZillaRepository;
import aminurdev.com.backend.domain.repository.UserRepository;
import aminurdev.com.backend.domain.request.UpZillaRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.UpZillaService;
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
public class UpZillaServiceImpl implements UpZillaService {

    private final UpZillaRepository upZillaRepository;
    private final DistrictRepository districtRepository;
    private final UserRepository userRepository;

    @Override
    public PaginationResponse<UpZilla> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<UpZilla> upZillaPage = upZillaRepository.findAll(pageable);
        List<UpZilla> upZillas = upZillaPage.getContent();

        PaginationResponse<UpZilla> response = new PaginationResponse<>();
        response.setData(upZillas);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All upzillas fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(upZillaPage.getNumber() + 1);
        meta.setFrom(upZillaPage.getNumber() * upZillaPage.getSize() + 1);
        meta.setLastPage(upZillaPage.getTotalPages());
        meta.setPath("/upzilla");
        meta.setPerPage(upZillaPage.getSize());
        meta.setTo((int) upZillaPage.getTotalElements());
        meta.setTotal((int) upZillaPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/upzilla?page=1");
        links.setLast("/upzilla?page=" + upZillaPage.getTotalPages());
        if (upZillaPage.hasPrevious()) {
            links.setPrev("/upzilla?page=" + upZillaPage.previousPageable().getPageNumber());
        }
        if (upZillaPage.hasNext()) {
            links.setNext("/upzilla?page=" + upZillaPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);
        return response;
    }

    @Override
    public List<UpZilla> getAllUpZillas() {
        return upZillaRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public UpZilla store(UpZillaRequest request) {
        try {
            District district = districtRepository.findById(request.getDistrictId())
                    .orElseThrow(() -> new RuntimeException("Zilla not found with ID: " + request.getDistrictId()));

            User user = userRepository.findById(request.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getCreatedBy()));

            UpZilla upZilla = UpZilla.builder()
                    .name_en(request.getName_en())
                    .name_bn(request.getName_bn())
                    .status(request.getStatus() != null ? request.getStatus() : false)
                    .district(district)
                    .createdBy(user.getId())
                    .build();

            return upZillaRepository.save(upZilla);

        } catch (Exception exception) {
            throw new GlobalException("Error while storing upzilla: " + exception.getMessage(), exception);
        }
    }

    @Override
    public UpZilla edit(Integer upzillaId) {
        return upZillaRepository.findById(upzillaId)
                .orElseThrow(() -> new RuntimeException("UpZilla id not found: " + upzillaId));
    }

    @Override
    public UpZilla update(UpZillaRequest request, Integer upzillaId) {
        try {
            UpZilla upZilla = upZillaRepository.findById(upzillaId)
                    .orElseThrow(() -> new RuntimeException("UpZilla id not found: " + upzillaId));

            if (request.getName_en() != null) {
                upZilla.setName_en(request.getName_en());
            }

            if (request.getName_bn() != null) {
                upZilla.setName_bn(request.getName_bn());
            }

            if (request.getDistrictId() != null) {
                District district = districtRepository.findById(request.getDistrictId())
                        .orElseThrow(() -> new RuntimeException("Zilla not found with ID: " + request.getDistrictId()));
                upZilla.setDistrict(district);
            }

            if (request.getStatus() != null) {
                upZilla.setStatus(request.getStatus());
            }

            return upZillaRepository.save(upZilla);

        } catch (Exception exception) {
            throw new GlobalException("Error while updating upzilla: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer upzillaId) {
        try {
            UpZilla upZilla = upZillaRepository.findById(upzillaId)
                    .orElseThrow(() -> new RuntimeException("UpZilla id not found: " + upzillaId));

            upZillaRepository.delete(upZilla);

        } catch (Exception exception) {
            throw new GlobalException("Error while deleting upzilla: " + exception.getMessage(), exception);
        }
    }
}
