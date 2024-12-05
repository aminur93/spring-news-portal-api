package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.request.UserRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserService {

    PaginationResponse<User> index(Sort.Direction direction, int page, int perPage);

    List<User> getAllUsers();

    User store(UserRequest request);

    User edit(Integer userId);

    User update(UserRequest request, Integer userId);

    void destroy(Integer userId);
}
