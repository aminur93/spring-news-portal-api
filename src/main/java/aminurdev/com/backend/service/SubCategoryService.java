package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.SubCategory;
import aminurdev.com.backend.domain.request.SubCategoryRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface SubCategoryService {

    PaginationResponse<SubCategory> index(Sort.Direction direction, int page, int perPage);

    List<SubCategory> getAllSubcategories();

    SubCategory store(SubCategoryRequest request);

    SubCategory edit(Integer subCategoryId);

    SubCategory update(Integer subCategoryId, SubCategoryRequest request);

    void destroy(Integer subCategoryId);
}
