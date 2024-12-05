package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.*;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.CategoryRepository;
import aminurdev.com.backend.domain.repository.NewsImagesRepository;
import aminurdev.com.backend.domain.repository.SubCategoryRepository;
import aminurdev.com.backend.domain.repository.TagRepository;
import aminurdev.com.backend.domain.request.NewsImagesRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.DigitalOceanSpacesService;
import aminurdev.com.backend.service.NewsImagesService;
import aminurdev.com.backend.webapp.config.DigitalOceanSpacesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsImagesServiceImpl implements NewsImagesService {

    private final NewsImagesRepository newsImagesRepository;

    private final CategoryRepository categoryRepository;

    private final SubCategoryRepository subCategoryRepository;

    private final TagRepository tagRepository;

    private final DigitalOceanSpacesService digitalOceanSpacesService;

    private final DigitalOceanSpacesConfig config;

    @Override
    public PaginationResponse<NewsImages> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<NewsImages> newsImagesPage = newsImagesRepository.findAll(pageable);
        List<NewsImages> newsImages = newsImagesPage.getContent();

        PaginationResponse<NewsImages> response = new PaginationResponse<>();
        response.setData(newsImages);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All news-images fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(newsImagesPage.getNumber() + 1);
        meta.setFrom(newsImagesPage.getNumber() * newsImagesPage.getSize() + 1);
        meta.setLastPage(newsImagesPage.getTotalPages());
        meta.setPath("/news-images");
        meta.setPerPage(newsImagesPage.getSize());
        meta.setTo((int) newsImagesPage.getTotalElements());
        meta.setTotal((int) newsImagesPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/news-images?page=1");
        links.setLast("/news-images?page=" + newsImagesPage.getTotalPages());
        if (newsImagesPage.hasPrevious()) {
            links.setPrev("/news-images?page=" + newsImagesPage.previousPageable().getPageNumber());
        }
        if (newsImagesPage.hasNext()) {
            links.setNext("/news-images?page=" + newsImagesPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<NewsImages> getAllNewsImages() {

        return newsImagesRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public NewsImages store(NewsImagesRequest request) {

        try {

            String imageUrl = null;

            NewsImages newsImages = new NewsImages();

            newsImages.setTitle_en(request.getTitle_en());
            newsImages.setTitle_bn(request.getTitle_bn());
            newsImages.setSlogan_en(request.getSlogan_en());
            newsImages.setSlogan_bn(request.getSlogan_bn());
            newsImages.setDescription_en(request.getDescription_en());
            newsImages.setDescription_bn(request.getDescription_bn());

            if (request.getImage() != null) { // Assuming request.getIcon() returns a MultipartFile
                MultipartFile iconFile = request.getImage(); // Get the MultipartFile
                String folderName = "news_images";  // Specify your desired folder name
                String objectKey = iconFile.getOriginalFilename(); // Use the original filename or generate a unique name

                // Call uploadImage method from DigitalOceanSpacesService
                imageUrl = digitalOceanSpacesService.uploadImage(iconFile, folderName, objectKey);
            }

            newsImages.setImage(imageUrl);
            newsImages.setStatus(request.isStatus());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                Integer userId = user.getId();  // Get the authenticated user ID

                // Set the 'created_by' field with the authenticated user's ID
                newsImages.setCreatedBy(userId);
            } else {
                throw new GlobalException("Unable to retrieve authenticated user");
            }

            //categories
            List<Integer> categoriesId = request.getCategoriesId();
            List<Category> categories = categoryRepository.findAllById(categoriesId);
            newsImages.setCategories(categories);

            //sub-categories
            List<Integer> subCategoriesId = request.getSubCategoriesId();
            List<SubCategory> subCategories = subCategoryRepository.findAllById(subCategoriesId);
            newsImages.setSubCategories(subCategories);

            //tags
            List<Integer> tagsId = request.getTagsId();
            List<TagEntity> tagEntities = tagRepository.findAllById(tagsId);
            newsImages.setTagEntities(tagEntities);

            return newsImagesRepository.save(newsImages);

        }catch (Exception exception){

            throw new GlobalException("Error while storing news images : " + exception.getMessage(), exception);
        }
    }

    @Override
    public NewsImages edit(Integer newsImageId) {

        return newsImagesRepository.findById(newsImageId).orElseThrow(() -> new RuntimeException("News images not found" + newsImageId));
    }

    @Override
    public NewsImages update(Integer newsImageId, NewsImagesRequest request) {

        try {

            NewsImages newsImages = newsImagesRepository.findById(newsImageId).orElseThrow(() -> new RuntimeException("News images not found" + newsImageId));

            newsImages.setTitle_en(request.getTitle_en());
            newsImages.setTitle_bn(request.getTitle_bn());
            newsImages.setSlogan_en(request.getSlogan_en());
            newsImages.setSlogan_bn(request.getSlogan_bn());
            newsImages.setDescription_en(request.getDescription_en());
            newsImages.setDescription_bn(request.getDescription_bn());

            // Check if a new icon is provided
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                // Delete the old icon from DigitalOcean Spaces if it exists
                if (newsImages.getImage() != null) {
                    String oldIconUrl = newsImages.getImage();
                    String oldIconKey = extractObjectKeyFromUrl(oldIconUrl);  // Extract the objectKey from the URL

                    // Call deleteIcon to remove the old icon
                    digitalOceanSpacesService.deleteImage(oldIconKey);
                }

                // Upload the new icon
                String folderName = "news_images";
                String objectKey = request.getImage().getOriginalFilename(); // Use the original filename or generate a unique one

                // Upload the new icon and get the new icon URL
                String newIconUrl = digitalOceanSpacesService.uploadImage(request.getImage(), folderName, objectKey);

                // Update the category with the new icon URL
                newsImages.setImage(newIconUrl);
            }

            newsImages.setStatus(request.isStatus());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                Integer userId = user.getId();  // Get the authenticated user ID

                // Set the 'created_by' field with the authenticated user's ID
                newsImages.setCreatedBy(userId);
            } else {
                throw new GlobalException("Unable to retrieve authenticated user");
            }

            //categories
            if (request.getCategoriesId() != null)
            {
                newsImages.getCategories().clear();

                List<Integer> categoriesId = request.getCategoriesId();
                List<Category> categories = categoryRepository.findAllById(categoriesId);
                newsImages.setCategories(categories);
            }


            //sub-categories
            if (request.getSubCategoriesId() != null)
            {
                newsImages.getSubCategories().clear();

                List<Integer> subCategoriesId = request.getSubCategoriesId();
                List<SubCategory> subCategories = subCategoryRepository.findAllById(subCategoriesId);
                newsImages.setSubCategories(subCategories);
            }


            //tags
            if (request.getTagsId() != null)
            {
                newsImages.getTagEntities().clear();

                List<Integer> tagsId = request.getTagsId();
                List<TagEntity> tagEntities = tagRepository.findAllById(tagsId);
                newsImages.setTagEntities(tagEntities);
            }


            return newsImagesRepository.save(newsImages);

        }catch (Exception exception){

            throw new GlobalException("Error while storing news images : " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer newsImageId) {

        NewsImages newsImages = newsImagesRepository.findById(newsImageId).orElseThrow(() -> new RuntimeException("News images id not found"));

        newsImages.getCategories().clear();
        newsImages.getSubCategories().clear();
        newsImages.getTagEntities().clear();

        newsImagesRepository.save(newsImages);

        newsImagesRepository.delete(newsImages);
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
