package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.Advertisement;
import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.AdvertisementRepository;
import aminurdev.com.backend.domain.repository.UserRepository;
import aminurdev.com.backend.domain.request.AdvertisementRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.AdvertisementService;
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
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    private final UserRepository userRepository;

    private final DigitalOceanSpacesService digitalOceanSpacesService;

    private final DigitalOceanSpacesConfig config;

    @Override
    public PaginationResponse<Advertisement> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<Advertisement> advertisementPage = advertisementRepository.findAll(pageable);

        List<Advertisement> advertisements = advertisementPage.getContent();

        PaginationResponse<Advertisement> response = new PaginationResponse<>();

        response.setData(advertisements);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All advertisements fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(advertisementPage.getNumber() + 1);
        meta.setFrom(advertisementPage.getNumber() * advertisementPage.getSize() + 1);
        meta.setLastPage(advertisementPage.getTotalPages());
        meta.setPath("/advertisement");
        meta.setPerPage(advertisementPage.getSize());
        meta.setTo((int) advertisementPage.getTotalElements());
        meta.setTotal((int) advertisementPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/advertisement?page=1");
        links.setLast("/advertisement?page=" + advertisementPage.getTotalPages());
        if (advertisementPage.hasPrevious()) {
            links.setPrev("/advertisement?page=" + advertisementPage.previousPageable().getPageNumber());
        }
        if (advertisementPage.hasNext()) {
            links.setNext("/advertisement?page=" + advertisementPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);
        return response;
    }

    @Override
    public List<Advertisement> getAllAdvertisements() {

        return advertisementRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Advertisement store(AdvertisementRequest request) {

        try {
            User user = userRepository.findById(request.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getCreatedBy()));

            // Handle image upload
            String imageUrl = null;
            if (request.getImage() != null) { // Assuming request.getImage() returns a MultipartFile
                MultipartFile imageFile = request.getImage(); // Get the MultipartFile
                String folderName = "advertisements";  // Specify your desired folder name
                String objectKey = imageFile.getOriginalFilename(); // Use the original filename or generate a unique name

                // Call uploadImage method from DigitalOceanSpacesService
                imageUrl = digitalOceanSpacesService.uploadImage(imageFile, folderName, objectKey);
            }

            Advertisement advertisement = Advertisement.builder()
                    .title_en(request.getTitle_en())
                    .title_bn(request.getTitle_bn())
                    .slogan_en(request.getSlogan_en())
                    .slogan_bn(request.getSlogan_bn())
                    .description_en(request.getDescription_en())
                    .description_bn(request.getDescription_bn())
                    .company_name_en(request.getCompany_name_en())
                    .company_name_bn(request.getCompany_name_bn())
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .image(imageUrl)
                    .status(request.getStatus() != null ? request.getStatus() : false)
                    .createdBy(user.getId())
                    .build();

            return advertisementRepository.save(advertisement);

        } catch (Exception exception) {

            throw new GlobalException("Error while storing advertisement: " + exception.getMessage(), exception);
        }
    }

    @Override
    public Advertisement edit(Integer advertisementId) {

        return advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new RuntimeException("Advertisement id is not found: " + advertisementId));
    }

    @Override
    public Advertisement update(AdvertisementRequest request, Integer advertisementId) {

        try {
            Advertisement advertisement = advertisementRepository.findById(advertisementId)
                    .orElseThrow(() -> new RuntimeException("Advertisement id is not found: " + advertisementId));

            if (request.getTitle_en() != null) {
                advertisement.setTitle_en(request.getTitle_en());
            }

            if (request.getTitle_bn() != null) {
                advertisement.setTitle_bn(request.getTitle_bn());
            }

            if (request.getSlogan_en() != null) {
                advertisement.setSlogan_en(request.getSlogan_en());
            }

            if (request.getSlogan_bn() != null) {
                advertisement.setSlogan_bn(request.getSlogan_bn());
            }

            if (request.getDescription_en() != null) {
                advertisement.setDescription_en(request.getDescription_en());
            }

            if (request.getDescription_bn() != null) {
                advertisement.setDescription_bn(request.getDescription_bn());
            }

            if (request.getCompany_name_en() != null) {
                advertisement.setCompany_name_en(request.getCompany_name_en());
            }

            if (request.getCompany_name_bn() != null) {
                advertisement.setCompany_name_bn(request.getCompany_name_bn());
            }

            if (request.getStartDate() != null) {
                advertisement.setStartDate(request.getStartDate());
            }

            if (request.getEndDate() != null) {
                advertisement.setEndDate(request.getEndDate());
            }

            // Check if a new image is provided
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                // Delete the old image from DigitalOcean Spaces if it exists
                if (advertisement.getImage() != null) {
                    String oldImageUrl = advertisement.getImage();
                    String oldImageKey = extractObjectKeyFromUrl(oldImageUrl);  // Extract the objectKey from the URL

                    // Call deleteImage to remove the old image
                    digitalOceanSpacesService.deleteImage(oldImageKey);
                }

                // Upload the new image
                String folderName = "advertisements";
                String objectKey = request.getImage().getOriginalFilename(); // Use the original filename or generate a unique one

                // Upload the new image and get the new image URL
                String newImageUrl = digitalOceanSpacesService.uploadImage(request.getImage(), folderName, objectKey);

                // Update the advertisement with the new image URL
                advertisement.setImage(newImageUrl);
            }


            if (request.getStatus() != null) {
                advertisement.setStatus(request.getStatus());
            }

            return advertisementRepository.save(advertisement);

        } catch (Exception exception) {

            throw new GlobalException("Error while updating advertisement: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer advertisementId) {

        try {
            Advertisement advertisement = advertisementRepository.findById(advertisementId)
                    .orElseThrow(() -> new RuntimeException("Advertisement id is not found: " + advertisementId));

            // Extract objectKey from the image URL (assuming the image is stored as a full URL)
            String imageUrl = advertisement.getImage();
            String objectKey = extractObjectKeyFromUrl(imageUrl); // Implement this method to extract the objectKey

            // Delete the image from DigitalOcean Spaces
            digitalOceanSpacesService.deleteImage(objectKey);

            advertisementRepository.delete(advertisement);

        } catch (Exception exception) {
            throw new GlobalException("Error while deleting advertisement: " + exception.getMessage(), exception);
        }
    }

    private String extractObjectKeyFromUrl(String imageUrl) {
        // Assuming the imageUrl is something like https://your-space.endpoint.com/bucketName/folderName/image.jpg
        // Extract the part after the bucket name, e.g., folderName/image.jpg
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Remove endpoint and bucket name part from the imageUrl
            String baseUrl = config.getEndpoint() + "/" + config.getBucketName() + "/";
            if (imageUrl.startsWith(baseUrl)) {
                return imageUrl.substring(baseUrl.length());  // Extract relative path (folderName/image.jpg)
            }
        }
        return null;
    }
}
