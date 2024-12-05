package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.Category;
import aminurdev.com.backend.domain.entity.SubCategory;
import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.CategoryRepository;
import aminurdev.com.backend.domain.repository.SubCategoryRepository;
import aminurdev.com.backend.domain.repository.UserRepository;
import aminurdev.com.backend.domain.request.SubCategoryRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.SubCategoryService;
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
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    @Override
    public PaginationResponse<SubCategory> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<SubCategory> subCategoryPage = subCategoryRepository.findAll(pageable);
        List<SubCategory> subCategories = subCategoryPage.getContent();

        PaginationResponse<SubCategory> response = new PaginationResponse<>();
        response.setData(subCategories);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Sub-categories fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(subCategoryPage.getNumber() + 1);
        meta.setFrom(subCategoryPage.getNumber() * subCategoryPage.getSize() + 1);
        meta.setLastPage(subCategoryPage.getTotalPages());
        meta.setPath("/sub-category");
        meta.setPerPage(subCategoryPage.getSize());
        meta.setTo((int) subCategoryPage.getTotalElements());
        meta.setTotal((int) subCategoryPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/sub-category?page=1");
        links.setLast("/sub-category?page=" + subCategoryPage.getTotalPages());
        if (subCategoryPage.hasPrevious()) {
            links.setPrev("/sub-category?page=" + subCategoryPage.previousPageable().getPageNumber());
        }
        if (subCategoryPage.hasNext()) {
            links.setNext("/sub-category?page=" + subCategoryPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<SubCategory> getAllSubcategories() {

        return subCategoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public SubCategory store(SubCategoryRequest request) {

        try {

            SubCategory subCategory = new SubCategory();

            // Fetch the Category based on the provided category ID
            Category category = categoryRepository.findById(request.getCategory_id())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            // Set the category in the SubCategory
            subCategory.setCategory(category); // Set the entire Category object
            subCategory.setName_en(request.getName_en());
            subCategory.setName_bn(request.getName_bn());
            subCategory.setDescription_en(request.getDescription_en());
            subCategory.setDescription_bn(request.getDescription_bn());
            subCategory.setStatus(request.getStatus() != null ? request.getStatus() : false);

            User user = userRepository.findById(request.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            subCategory.setCreatedBy(user.getId());

            return subCategoryRepository.save(subCategory);

        }catch (Exception exception){

            throw new GlobalException("Error while storing: " + exception.getMessage(), exception);
        }
    }

    @Override
    public SubCategory edit(Integer subCategoryId) {

        return subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new RuntimeException("Sub category not found"));
    }

    @Override
    public SubCategory update(Integer subCategoryId, SubCategoryRequest request) {

        try {

            SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new RuntimeException("Sub category not found"));

            Category category = categoryRepository.findById(request.getCategory_id())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            subCategory.setCategory(category);
            subCategory.setName_en(request.getName_en());
            subCategory.setName_bn(request.getName_bn());
            subCategory.setDescription_en(request.getDescription_en());
            subCategory.setDescription_bn(request.getDescription_bn());
            subCategory.setStatus(request.getStatus() != null ? request.getStatus() : false);

            User user = userRepository.findById(request.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            subCategory.setCreatedBy(user.getId());

            return subCategoryRepository.save(subCategory);

        }catch (Exception exception){

            throw new GlobalException("Error while updating sub-category : " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer subCategoryId) {

        subCategoryRepository.deleteById(subCategoryId);
    }
}
