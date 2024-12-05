package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.*;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.CategoryRepository;
import aminurdev.com.backend.domain.repository.NewsVideosRepository;
import aminurdev.com.backend.domain.repository.SubCategoryRepository;
import aminurdev.com.backend.domain.repository.TagRepository;
import aminurdev.com.backend.domain.request.NewsVideosRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.DigitalOceanSpacesService;
import aminurdev.com.backend.service.NewsVideoService;
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
public class NewsVideoServiceImpl implements NewsVideoService {

    private final NewsVideosRepository newsVideosRepository;

    private final CategoryRepository categoryRepository;

    private final SubCategoryRepository subCategoryRepository;

    private final TagRepository tagRepository;

    private final DigitalOceanSpacesService digitalOceanSpacesService;

    private final DigitalOceanSpacesConfig config;

    @Override
    public PaginationResponse<NewsVideo> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<NewsVideo> newsVideoPage = newsVideosRepository.findAll(pageable);
        List<NewsVideo> newsVideos = newsVideoPage.getContent();

        PaginationResponse<NewsVideo> response = new PaginationResponse<>();
        response.setData(newsVideos);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All news-videos fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(newsVideoPage.getNumber() + 1);
        meta.setFrom(newsVideoPage.getNumber() * newsVideoPage.getSize() + 1);
        meta.setLastPage(newsVideoPage.getTotalPages());
        meta.setPath("/news-video");
        meta.setPerPage(newsVideoPage.getSize());
        meta.setTo((int) newsVideoPage.getTotalElements());
        meta.setTotal((int) newsVideoPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/news-video?page=1");
        links.setLast("/news-video?page=" + newsVideoPage.getTotalPages());
        if (newsVideoPage.hasPrevious()) {
            links.setPrev("/news-video?page=" + newsVideoPage.previousPageable().getPageNumber());
        }
        if (newsVideoPage.hasNext()) {
            links.setNext("/news-video?page=" + newsVideoPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<NewsVideo> getAllNewsVideos() {

        return newsVideosRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public NewsVideo store(NewsVideosRequest request) {

        try {

            String videoUrl = null;

            NewsVideo newsVideo = new NewsVideo();

            newsVideo.setTitle_en(request.getTitle_en());
            newsVideo.setTitle_bn(request.getTitle_bn());
            newsVideo.setSlogan_en(request.getSlogan_en());
            newsVideo.setSlogan_bn(request.getSlogan_bn());
            newsVideo.setDescription_en(request.getDescription_en());
            newsVideo.setDescription_bn(request.getDescription_bn());

            if (request.getVideo() != null) { // Assuming request.getIcon() returns a MultipartFile
                MultipartFile iconFile = request.getVideo(); // Get the MultipartFile
                String folderName = "news_videos";  // Specify your desired folder name
                String objectKey = iconFile.getOriginalFilename(); // Use the original filename or generate a unique name

                // Call uploadImage method from DigitalOceanSpacesService
                videoUrl = digitalOceanSpacesService.uploadImage(iconFile, folderName, objectKey);
            }

            newsVideo.setVideo(videoUrl);
            newsVideo.setStatus(request.isStatus());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                Integer userId = user.getId();  // Get the authenticated user ID

                // Set the 'created_by' field with the authenticated user's ID
                newsVideo.setCreatedBy(userId);
            } else {
                throw new GlobalException("Unable to retrieve authenticated user");
            }

            //categories
            List<Integer> categoriesId = request.getCategoriesId();
            List<Category> categories = categoryRepository.findAllById(categoriesId);
            newsVideo.setCategories(categories);

            //sub-categories
            List<Integer> subCategoriesId = request.getSubCategoriesId();
            List<SubCategory> subCategories = subCategoryRepository.findAllById(subCategoriesId);
            newsVideo.setSubCategories(subCategories);

            //tags
            List<Integer> tagsId = request.getTagsId();
            List<TagEntity> tagEntities = tagRepository.findAllById(tagsId);
            newsVideo.setTagEntities(tagEntities);

            return newsVideosRepository.save(newsVideo);

        }catch (Exception exception){

            throw new GlobalException("Error while storing news video : " + exception.getMessage(), exception);
        }
    }

    @Override
    public NewsVideo edit(Integer newsVideoId) {

        return newsVideosRepository.findById(newsVideoId).orElseThrow(() -> new RuntimeException("News video id not found " + newsVideoId));
    }

    @Override
    public NewsVideo update(Integer newsVideoId, NewsVideosRequest request) {

        try {

            NewsVideo newsVideo = newsVideosRepository.findById(newsVideoId).orElseThrow(() -> new RuntimeException("News video id not found " + newsVideoId));

            newsVideo.setTitle_en(request.getTitle_en());
            newsVideo.setTitle_bn(request.getTitle_bn());
            newsVideo.setSlogan_en(request.getSlogan_en());
            newsVideo.setSlogan_bn(request.getSlogan_bn());
            newsVideo.setDescription_en(request.getDescription_en());
            newsVideo.setDescription_bn(request.getDescription_bn());

            // Check if a new icon is provided
            if (request.getVideo() != null && !request.getVideo().isEmpty()) {
                // Delete the old icon from DigitalOcean Spaces if it exists
                if (newsVideo.getVideo() != null) {
                    String oldIconUrl = newsVideo.getVideo();
                    String oldIconKey = extractObjectKeyFromUrl(oldIconUrl);  // Extract the objectKey from the URL

                    // Call deleteIcon to remove the old icon
                    digitalOceanSpacesService.deleteImage(oldIconKey);
                }

                // Upload the new icon
                String folderName = "news_videos";
                String objectKey = request.getVideo().getOriginalFilename(); // Use the original filename or generate a unique one

                // Upload the new icon and get the new icon URL
                String newVideoUrl = digitalOceanSpacesService.uploadImage(request.getVideo(), folderName, objectKey);

                // Update the category with the new icon URL
                newsVideo.setVideo(newVideoUrl);
            }

            newsVideo.setStatus(request.isStatus());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();
                Integer userId = user.getId();  // Get the authenticated user ID

                // Set the 'created_by' field with the authenticated user's ID
                newsVideo.setCreatedBy(userId);
            } else {
                throw new GlobalException("Unable to retrieve authenticated user");
            }

            //categories
            if (request.getCategoriesId() != null)
            {
                newsVideo.getCategories().clear();

                List<Integer> categoriesId = request.getCategoriesId();
                List<Category> categories = categoryRepository.findAllById(categoriesId);
                newsVideo.setCategories(categories);
            }


            //sub-categories
            if (request.getSubCategoriesId() != null)
            {
                newsVideo.getSubCategories().clear();

                List<Integer> subCategoriesId = request.getSubCategoriesId();
                List<SubCategory> subCategories = subCategoryRepository.findAllById(subCategoriesId);
                newsVideo.setSubCategories(subCategories);
            }


            //tags
            if (request.getTagsId() != null)
            {
                newsVideo.getTagEntities().clear();

                List<Integer> tagsId = request.getTagsId();
                List<TagEntity> tagEntities = tagRepository.findAllById(tagsId);
                newsVideo.setTagEntities(tagEntities);
            }


            return newsVideosRepository.save(newsVideo);

        }catch (Exception exception){

            throw new GlobalException("Error while storing news video : " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer newsVideoId) {

        NewsVideo newsVideo = newsVideosRepository.findById(newsVideoId).orElseThrow(() -> new RuntimeException("News videos id not found"));

        newsVideo.getCategories().clear();
        newsVideo.getSubCategories().clear();
        newsVideo.getTagEntities().clear();

        newsVideosRepository.save(newsVideo);

        newsVideosRepository.delete(newsVideo);
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
