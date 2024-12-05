package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.Permission;
import aminurdev.com.backend.domain.entity.Role;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.exception.ResourceNotFoundException;
import aminurdev.com.backend.domain.repository.PermissionRepository;
import aminurdev.com.backend.domain.repository.RoleRepository;
import aminurdev.com.backend.domain.request.RoleRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    @Override
    public PaginationResponse<Role> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction,"id"));

        Page<Role> rolePage = roleRepository.findAll(pageable);

        List<Role> roles = rolePage.getContent();

        PaginationResponse<Role> response = new PaginationResponse<>();

        response.setData(roles);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All role fetch successful");

        Meta meta = new Meta();

        meta.setCurrentPage(rolePage.getNumber() + 1);
        meta.setFrom(rolePage.getNumber() * rolePage.getSize() + 1);
        meta.setLastPage(rolePage.getTotalPages());
        meta.setPath("/role");
        meta.setPerPage(rolePage.getSize());
        meta.setTo((int) rolePage.getTotalElements());
        meta.setTotal((int) rolePage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();

        links.setFirst("/role?page=1");
        links.setLast("/role?page=" + rolePage.getTotalPages());
        if (rolePage.hasPrevious()) {
            links.setPrev("/role?page=" + rolePage.previousPageable().getPageNumber());
        }
        if (rolePage.hasNext()) {
            links.setNext("/role?page=" + rolePage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<Role> getAllRoles() {

        return roleRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Role store(RoleRequest request) {

        try {

            Role role = Role.builder()
                    .name_en(request.getName_en())
                    .name_bn(request.getName_bn())
                    .build();

            Role savedRole = roleRepository.save(role);

            List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());

            savedRole.setPermissions(permissions);

            return roleRepository.save(savedRole);

        }catch (Exception exception){

            throw new GlobalException("Error while storing role: " + exception.getMessage(), exception);
        }
    }

    @Override
    public Map<String, Object> edit(Integer roleId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        // Transform the data similar to the Laravel mapToGroups
        Map<String, Object> transformedRole = new HashMap<>();
        transformedRole.put("id", role.getId());
        transformedRole.put("role", role.getName_en());

        // Grouping permissions by title and creating a map of id and name
        Map<String, List<Map<String, Object>>> permissions = role.getPermissions().stream()
                .collect(Collectors.groupingBy(
                        Permission::getName_en,
                        Collectors.mapping(permission -> {
                            Map<String, Object> permissionData = new HashMap<>();
                            permissionData.put("id", permission.getId());
                            permissionData.put("name_en", permission.getName_en());
                            permissionData.put("name_bn", permission.getName_bn());
                            return permissionData;
                        }, Collectors.toList())
                ));

        transformedRole.put("permissions", permissions);
        return transformedRole;
    }

    @Override
    public Role update(RoleRequest request, Integer roleId) {

        try{

            Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role id is not found : " + roleId));

            if (request.getName_en() != null)
            {
                role.setName_en(request.getName_en());
            }

            if (request.getName_bn() != null)
            {
                role.setName_bn(request.getName_bn());
            }

            role.getPermissions().clear();

            List<Permission> newPermissions = permissionRepository.findAllById(request.getPermissions());

            role.getPermissions().addAll(newPermissions);

            return roleRepository.save(role);

        }catch (Exception exception){

            throw new GlobalException("Error while update role : " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer roleId) {

        try {
            Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role id is not found : " + roleId));

            if (role.getId() != null)
            {
                roleRepository.deleteRolePermissions(roleId);
            }

            roleRepository.delete(role);

        }catch (Exception exception){

            throw new GlobalException("Error while role delete: " + exception.getMessage(), exception);
        }
    }
}
