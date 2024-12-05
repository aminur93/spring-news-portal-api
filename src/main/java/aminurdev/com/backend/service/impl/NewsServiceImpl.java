package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.*;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.*;
import aminurdev.com.backend.domain.request.NewsRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.DigitalOceanSpacesService;
import aminurdev.com.backend.service.NewsService;
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
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    private final CategoryRepository categoryRepository;

    private final SubCategoryRepository subCategoryRepository;

    private final TagRepository tagRepository;

    private final CountryRepository countryRepository;

    private final DivisionRepository divisionRepository;

    private final DistrictRepository districtRepository;

    private final UpZillaRepository upZillaRepository;

    private final DigitalOceanSpacesService digitalOceanSpacesService;

    private final DigitalOceanSpacesConfig config;

    @Override
    public PaginationResponse<News> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction,"updatedAt"));

        Page<News> newsPage = newsRepository.findAll(pageable);

        List<News> news = newsPage.getContent();

        PaginationResponse<News> response = new PaginationResponse<>();

        response.setData(news);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All News fetch successful");

        Meta meta = new Meta();

        meta.setCurrentPage(newsPage.getNumber() + 1);
        meta.setFrom(newsPage.getNumber() * newsPage.getSize() + 1);
        meta.setLastPage(newsPage.getTotalPages());
        meta.setPath("/news");
        meta.setPerPage(newsPage.getSize());
        meta.setTo((int) newsPage.getTotalElements());
        meta.setTotal((int) newsPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();

        links.setFirst("/news?page=1");
        links.setLast("/news?page=" + newsPage.getTotalPages());
        if (newsPage.hasPrevious()) {
            links.setPrev("/news?page=" + newsPage.previousPageable().getPageNumber());
        }
        if (newsPage.hasNext()) {
            links.setNext("/news?page=" + newsPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<News> getAllNews() {

        return newsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

    }

    @Override
    public News store(NewsRequest request) {

        try {

            News news = new News();

            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new RuntimeException("Category id not found " + request.getCategoryId()));

            news.setCategory(category);
            news.setHeadingEn(request.getHeadingEn());
            news.setHeadingBn(request.getHeadingBn());
            news.setTitleEn(request.getTitleEn());
            news.setTitleBn(request.getTitleBn());
            news.setDescriptionEn(request.getDescriptionEn());
            news.setDescriptionBn(request.getDescriptionBn());
            news.setAdditionalDescriptionEn(request.getAdditionalDescriptionEn());
            news.setAdditionalDescriptionBn(request.getAdditionalDescriptionBn());

            // Handle cover image upload
            String coverImageUrl = null;
            if (request.getCoverImage() != null) { // Assuming request.getIcon() returns a MultipartFile
                MultipartFile coverImageFile = request.getCoverImage(); // Get the MultipartFile
                String folderName = "news_cover_image";  // Specify your desired folder name
                String objectKey = coverImageFile.getOriginalFilename(); // Use the original filename or generate a unique name

                // Call uploadImage method from DigitalOceanSpacesService
                coverImageUrl = digitalOceanSpacesService.uploadImage(coverImageFile, folderName, objectKey);
            }

            news.setCoverImage(coverImageUrl);

            // Handle cover image upload
            String imageUrl = null;
            if (request.getImage() != null) { // Assuming request.getIcon() returns a MultipartFile
                MultipartFile imageFile = request.getImage(); // Get the MultipartFile
                String folderName = "news_image";  // Specify your desired folder name
                String objectKey = imageFile.getOriginalFilename(); // Use the original filename or generate a unique name

                // Call uploadImage method from DigitalOceanSpacesService
                imageUrl = digitalOceanSpacesService.uploadImage(imageFile, folderName, objectKey);
            }

            news.setImage(imageUrl);

            news.setDate(request.getDate());
            news.setSourceEn(request.getSourceEn());
            news.setSourceBn(request.getSourceBn());
            news.setNewsColumn(request.getNewsColumn());
            news.setIsPopular(request.getIsPopular());
            news.setIsBreaking(request.getIsBreaking());
            news.setIsOpinion(request.getIsOpinion());
            news.setIsForYou(request.getIsForYou());
            news.setIsDiscussed(request.getIsDiscussed());
            news.setIsGoodNews(request.getIsGoodNews());
            news.setIsBd(request.getIsBd());
            news.setIsWorld(request.getIsWorld());
            news.setIsTop(request.getIsTop());
            news.setIsMiddle(request.getIsMiddle());
            news.setIsBottom(request.getIsBottom());
            news.setIsFeatured(request.getIsFeatured());
            news.setIsFeatured(request.getIsFeatured());
            news.setIsFashion(request.getIsFashion());
            news.setIsCartoon(request.getIsCartoon());
            news.setCount(0);
            news.setStatus(request.getStatus());
            news.setCreatedBy(request.getCreatedBy());

            Country country = countryRepository.findById(request.getCountryId()).orElseThrow(() -> new RuntimeException("Country id not found"));

            news.setCountry(country);

            Division division = divisionRepository.findById(request.getDivisionId()).orElseThrow(() -> new RuntimeException("Division id not found"));

            news.setDivision(division);

            District district = districtRepository.findById(request.getDistrictId()).orElseThrow(() -> new RuntimeException("Division id not found"));

            news.setDistrict(district);

            UpZilla upZilla = upZillaRepository.findById(request.getUpzillaId()).orElseThrow(() -> new RuntimeException("Division id not found"));

            news.setUpzilla(upZilla);

            //sub-categories
            List<Integer> subCategoriesId = request.getSubCategoryIds();
            List<SubCategory> subCategories = subCategoryRepository.findAllById(subCategoriesId);
            news.setSubCategories(subCategories);

            //tags
            List<Integer> tagsId = request.getTagIds();
            List<TagEntity> tagEntities = tagRepository.findAllById(tagsId);
            news.setTagEntities(tagEntities);


            return newsRepository.save(news);

        }catch (Exception exception){

            throw new GlobalException("Error while news storing : " + exception.getMessage(), exception);
        }
    }

    @Override
    public News edit(Integer newsId) {

        return newsRepository.findById(newsId).orElseThrow(() -> new RuntimeException("News id is not found"));
    }

    @Override
    public News update(Integer newsId, NewsRequest request) {

        try {

            News news = newsRepository.findById(newsId).orElseThrow(() -> new RuntimeException("News id is not found"));

            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new RuntimeException("Category id not found " + request.getCategoryId()));

            news.setCategory(category);
            news.setHeadingEn(request.getHeadingEn());
            news.setHeadingBn(request.getHeadingBn());
            news.setTitleEn(request.getTitleEn());
            news.setTitleBn(request.getTitleBn());
            news.setDescriptionEn(request.getDescriptionEn());
            news.setDescriptionBn(request.getDescriptionBn());
            news.setAdditionalDescriptionEn(request.getAdditionalDescriptionEn());
            news.setAdditionalDescriptionBn(request.getAdditionalDescriptionBn());

            // Check if a news cover image is provided
            if (request.getCoverImage() != null && !request.getCoverImage().isEmpty()) {
                // Delete the old icon from DigitalOcean Spaces if it exists
                if (news.getCoverImage() != null) {
                    String oldCoverImageUrl = news.getCoverImage();
                    String oldCoverImageKey = extractObjectKeyFromUrl(oldCoverImageUrl);  // Extract the objectKey from the URL

                    // Call deleteIcon to remove the old icon
                    digitalOceanSpacesService.deleteImage(oldCoverImageKey);
                }

                // Upload the new icon
                String folderName = "news_cover_image";
                String objectKey = request.getCoverImage().getOriginalFilename(); // Use the original filename or generate a unique one

                // Upload the new icon and get the new icon URL
                String newCoverImageUrl = digitalOceanSpacesService.uploadImage(request.getCoverImage(), folderName, objectKey);

                // Update the category with the new icon URL
                news.setCoverImage(newCoverImageUrl);
            }

            // Check if a news cover image is provided
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                // Delete the old icon from DigitalOcean Spaces if it exists
                if (news.getImage() != null) {
                    String oldImageUrl = news.getImage();
                    String oldImageKey = extractObjectKeyFromUrl(oldImageUrl);  // Extract the objectKey from the URL

                    // Call deleteIcon to remove the old icon
                    digitalOceanSpacesService.deleteImage(oldImageKey);
                }

                // Upload the new icon
                String folderName = "news_image";
                String objectKey = request.getImage().getOriginalFilename(); // Use the original filename or generate a unique one

                // Upload the new icon and get the new icon URL
                String newImageUrl = digitalOceanSpacesService.uploadImage(request.getImage(), folderName, objectKey);

                // Update the category with the new icon URL
                news.setImage(newImageUrl);
            }

            news.setDate(request.getDate());
            news.setSourceEn(request.getSourceEn());
            news.setSourceBn(request.getSourceBn());
            news.setNewsColumn(request.getNewsColumn());
            news.setIsPopular(request.getIsPopular());
            news.setIsBreaking(request.getIsBreaking());
            news.setIsOpinion(request.getIsOpinion());
            news.setIsForYou(request.getIsForYou());
            news.setIsDiscussed(request.getIsDiscussed());
            news.setIsGoodNews(request.getIsGoodNews());
            news.setIsBd(request.getIsBd());
            news.setIsWorld(request.getIsWorld());
            news.setIsTop(request.getIsTop());
            news.setIsMiddle(request.getIsMiddle());
            news.setIsBottom(request.getIsBottom());
            news.setIsFeatured(request.getIsFeatured());
            news.setIsFeatured(request.getIsFeatured());
            news.setIsFashion(request.getIsFashion());
            news.setIsCartoon(request.getIsCartoon());
            news.setCount(0);
            news.setStatus(request.getStatus());
            news.setCreatedBy(request.getCreatedBy());

            Country country = countryRepository.findById(request.getCountryId()).orElseThrow(() -> new RuntimeException("Country id not found"));

            news.setCountry(country);

            Division division = divisionRepository.findById(request.getDivisionId()).orElseThrow(() -> new RuntimeException("Division id not found"));

            news.setDivision(division);

            District district = districtRepository.findById(request.getDistrictId()).orElseThrow(() -> new RuntimeException("Division id not found"));

            news.setDistrict(district);

            UpZilla upZilla = upZillaRepository.findById(request.getUpzillaId()).orElseThrow(() -> new RuntimeException("Division id not found"));

            news.setUpzilla(upZilla);

            //sub-categories
            List<Integer> subCategoriesId = request.getSubCategoryIds();
            List<SubCategory> subCategories = subCategoryRepository.findAllById(subCategoriesId);
            news.setSubCategories(subCategories);

            //tags
            List<Integer> tagsId = request.getTagIds();
            List<TagEntity> tagEntities = tagRepository.findAllById(tagsId);
            news.setTagEntities(tagEntities);

            return newsRepository.save(news);

        }catch (Exception exception){

            throw new GlobalException("Error while news update : " + exception.getMessage(), exception);
        }
    }

    @Override
    public News count(Integer newsId) {

        News news = newsRepository.findById(newsId).orElseThrow(() -> new RuntimeException("News id is not found"));

        news.setCount(news.getCount() + 1);

        return newsRepository.save(news);
    }

    @Override
    public void destroy(Integer newsId) {

        News news = newsRepository.findById(newsId).orElseThrow(() -> new RuntimeException("News id is not found"));

        news.getSubCategories().clear();
        news.getTagEntities().clear();

        if (news.getCoverImage() != null)
        {
            // Extract objectKey from the icon URL (assuming the icon is stored as a full URL)
            String iconUrl = news.getCoverImage();
            String objectKey = extractObjectKeyFromUrl(iconUrl); // Implement this method to extract the objectKey

            // Delete the icon from DigitalOcean Spaces
            digitalOceanSpacesService.deleteImage(objectKey);
        }

        if (news.getImage() != null)
        {
            // Extract objectKey from the icon URL (assuming the icon is stored as a full URL)
            String iconUrl = news.getCoverImage();
            String objectKey = extractObjectKeyFromUrl(iconUrl); // Implement this method to extract the objectKey

            // Delete the icon from DigitalOcean Spaces
            digitalOceanSpacesService.deleteImage(objectKey);
        }

        newsRepository.save(news);

        newsRepository.delete(news);
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
