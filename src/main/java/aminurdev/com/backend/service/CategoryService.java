package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.Category;
import aminurdev.com.backend.domain.request.CategoryRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CategoryService {

    PaginationResponse<Category> index(Sort.Direction direction, int page, int perPage);

    List<Category> getAllCategories();

    Category store(CategoryRequest request);

    Category edit(Integer categoryId);

    Category update(CategoryRequest request, Integer categoryId);

    void destroy(Integer categoryId);

}
