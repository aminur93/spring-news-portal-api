package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.Menu;
import aminurdev.com.backend.domain.request.MenuRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface MenuService {

    PaginationResponse<Menu> index(Sort.Direction direction, int page, int perPage);

    List<Menu> getAllMenus();

    Menu store(MenuRequest request);

    Menu edit(Integer menuId);

    Menu update(Integer menuId, MenuRequest request);

    void destroy(Integer menuId);
}
