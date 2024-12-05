package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.Permission;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.exception.ResourceNotFoundException;
import aminurdev.com.backend.domain.repository.PermissionRepository;
import aminurdev.com.backend.domain.request.PermissionRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public PaginationResponse<Permission> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction,"updatedAt"));

        Page<Permission> permissionPage = permissionRepository.findAll(pageable);

        List<Permission> permissions = permissionPage.getContent();

        PaginationResponse<Permission> response = new PaginationResponse<>();

        response.setData(permissions);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All permissions fetch successful");

        Meta meta = new Meta();

        meta.setCurrentPage(permissionPage.getNumber() + 1);
        meta.setFrom(permissionPage.getNumber() * permissionPage.getSize() + 1);
        meta.setLastPage(permissionPage.getTotalPages());
        meta.setPath("/permission");
        meta.setPerPage(permissionPage.getSize());
        meta.setTo((int) permissionPage.getTotalElements());
        meta.setTotal((int) permissionPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();

        links.setFirst("/permission?page=1");
        links.setLast("/permission?page=" + permissionPage.getTotalPages());
        if (permissionPage.hasPrevious()) {
            links.setPrev("/permission?page=" + permissionPage.previousPageable().getPageNumber());
        }
        if (permissionPage.hasNext()) {
            links.setNext("/permission?page=" + permissionPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<Map<String, Object>> getAllPermission() {

        return permissionRepository.findPermissionsGroupedByTitle();
    }

    @Override
    public Permission store(PermissionRequest request) {

        try{

            Permission permission = new Permission();

            permission.setTitle_en(request.getTitle_en());
            permission.setTitle_bn(request.getTitle_bn());

            permission.setName_en(request.getName_en());
            permission.setName_bn(request.getName_bn());

            permissionRepository.save(permission);

            return permission;

        } catch (Exception e) {

            throw new GlobalException("Error while storing permission: "+ e.getMessage(), e);
        }
    }

    @Override
    public Permission edit(Integer permissionId) {

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new GlobalException("Permission not found"));

        return permission;
    }

    @Override
    public Permission update(PermissionRequest request, Integer permissionId) {

        try{

            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new GlobalException("Permission id is not found" + permissionId));

            permission.setTitle_en(request.getTitle_en());
            permission.setTitle_bn(request.getTitle_bn());

            permission.setName_en(request.getName_en());
            permission.setName_bn(request.getName_bn());

            permissionRepository.save(permission);

            return permission;

        } catch (Exception e) {

            throw new GlobalException("Error while updating permission: " + e.getMessage(), e);
        }
    }

    @Override
    public void destroy(Integer permissionId) {

        permissionRepository.deleteById(permissionId);
    }
}
