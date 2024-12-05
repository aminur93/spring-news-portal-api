package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.Permission;
import aminurdev.com.backend.domain.request.PermissionRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public interface PermissionService {

    PaginationResponse<Permission> index(Sort.Direction direction, int page, int perPage);

    List<Map<String, Object>> getAllPermission();

    Permission store(PermissionRequest request);

    Permission edit(Integer permissionId);

    Permission update(PermissionRequest request, Integer permissionId);

    void destroy(Integer permissionId);
}
