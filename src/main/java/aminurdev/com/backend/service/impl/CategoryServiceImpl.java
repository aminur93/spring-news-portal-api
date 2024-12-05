package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.Category;
import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.CategoryRepository;
import aminurdev.com.backend.domain.repository.UserRepository;
import aminurdev.com.backend.domain.request.CategoryRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.CategoryService;
import aminurdev.com.backend.service.DigitalOceanSpacesService;
import aminurdev.com.backend.webapp.config.DigitalOceanSpacesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final DigitalOceanSpacesService digitalOceanSpacesService;

    private final DigitalOceanSpacesConfig config;

    @Override
    public PaginationResponse<Category> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<Category> categories = categoryPage.getContent();

        PaginationResponse<Category> response = new PaginationResponse<>();
        response.setData(categories);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All categories fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(categoryPage.getNumber() + 1);
        meta.setFrom(categoryPage.getNumber() * categoryPage.getSize() + 1);
        meta.setLastPage(categoryPage.getTotalPages());
        meta.setPath("/category");
        meta.setPerPage(categoryPage.getSize());
        meta.setTo((int) categoryPage.getTotalElements());
        meta.setTotal((int) categoryPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/category?page=1");
        links.setLast("/category?page=" + categoryPage.getTotalPages());
        if (categoryPage.hasPrevious()) {
            links.setPrev("/category?page=" + categoryPage.previousPageable().getPageNumber());
        }
        if (categoryPage.hasNext()) {
            links.setNext("/category?page=" + categoryPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);
        return response;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Category store(CategoryRequest request) {

        try {
            User user = userRepository.findById(request.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getCreatedBy()));

            // Handle icon upload
            String iconUrl = null;
            if (request.getIcon() != null) { // Assuming request.getIcon() returns a MultipartFile
                MultipartFile iconFile = request.getIcon(); // Get the MultipartFile
                String folderName = "categories";  // Specify your desired folder name
                String objectKey = iconFile.getOriginalFilename(); // Use the original filename or generate a unique name

                // Call uploadImage method from DigitalOceanSpacesService
                iconUrl = digitalOceanSpacesService.uploadImage(iconFile, folderName, objectKey);
            }

            Category category = Category.builder()
                    .name_en(request.getName_en())
                    .name_bn(request.getName_bn())
                    .description_en(request.getDescription_en())
                    .description_bn(request.getDescription_bn())
                    .icon(iconUrl)
                    .status(request.getStatus() != null ? request.getStatus() : false)
                    .createdBy(user.getId())
                    .build();

            return categoryRepository.save(category);

        } catch (Exception exception) {

            throw new GlobalException("Error while storing category: " + exception.getMessage(), exception);
        }
    }

    @Override
    public Category edit(Integer categoryId) {

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category id is not found: " + categoryId));
    }

    @Override
    public Category update(CategoryRequest request, Integer categoryId) {

        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category id is not found: " + categoryId));

            category.setName_en(request.getName_en());
            category.setName_bn(request.getName_bn());

            category.setDescription_en(request.getDescription_en());
            category.setDescription_bn(request.getDescription_bn());


            // Check if a new icon is provided
            if (request.getIcon() != null && !request.getIcon().isEmpty()) {
                // Delete the old icon from DigitalOcean Spaces if it exists
                if (category.getIcon() != null) {
                    String oldIconUrl = category.getIcon();
                    String oldIconKey = extractObjectKeyFromUrl(oldIconUrl);  // Extract the objectKey from the URL

                    // Call deleteIcon to remove the old icon
                    digitalOceanSpacesService.deleteImage(oldIconKey);
                }

                // Upload the new icon
                String folderName = "categories";
                String objectKey = request.getIcon().getOriginalFilename(); // Use the original filename or generate a unique one

                // Upload the new icon and get the new icon URL
                String newIconUrl = digitalOceanSpacesService.uploadImage(request.getIcon(), folderName, objectKey);

                // Update the category with the new icon URL
                category.setIcon(newIconUrl);
            }


            category.setStatus(request.getStatus());

            return categoryRepository.save(category);

        } catch (Exception exception) {

            throw new GlobalException("Error while updating category: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer categoryId) {

        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category id is not found: " + categoryId));

            // Extract objectKey from the icon URL (assuming the icon is stored as a full URL)
            String iconUrl = category.getIcon();
            String objectKey = extractObjectKeyFromUrl(iconUrl); // Implement this method to extract the objectKey

            // Delete the icon from DigitalOcean Spaces
            digitalOceanSpacesService.deleteImage(objectKey);

            categoryRepository.delete(category);

        } catch (Exception exception) {
            throw new GlobalException("Error while deleting category: " + exception.getMessage(), exception);
        }
    }

    private String extractObjectKeyFromUrl(String iconUrl) {
        // Assuming the iconUrl is something like https://your-space.endpoint.com/bucketName/folderName/image.jpg
        // Extract the part after the bucket name, e.g., folderName/image.jpg
        if (iconUrl != null && !iconUrl.isEmpty()) {
            // Remove endpoint and bucket name part from the iconUrl
            String baseUrl = config.getEndpoint() + "/" + config.getBucketName() + "/";
            if (iconUrl.startsWith(baseUrl)) {
                return iconUrl.substring(baseUrl.length());  // Extract relative path (folderName/image.jpg)
            }
        }
        return null;
    }

}
