package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.Role;
import aminurdev.com.backend.domain.request.RoleRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public interface RoleService {

    PaginationResponse<Role> index(Sort.Direction direction, int page, int perPage);

    List<Role> getAllRoles();

    Role store(RoleRequest request);

    Map<String, Object> edit(Integer roleId);

    Role update(RoleRequest request, Integer roleId);

    void destroy(Integer roleId);
}
