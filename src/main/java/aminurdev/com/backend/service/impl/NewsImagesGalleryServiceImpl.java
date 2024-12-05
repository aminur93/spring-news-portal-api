package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.NewsImages;
import aminurdev.com.backend.domain.entity.NewsImagesGallery;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.NewsImagesGalleryRepository;
import aminurdev.com.backend.domain.repository.NewsImagesRepository;
import aminurdev.com.backend.domain.request.NewsImagesGalleryRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.DigitalOceanSpacesService;
import aminurdev.com.backend.service.NewsImagesGalleryService;
import aminurdev.com.backend.webapp.config.DigitalOceanSpacesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsImagesGalleryServiceImpl implements NewsImagesGalleryService {

    private final NewsImagesGalleryRepository newsImagesGalleryRepository;

    private final NewsImagesRepository newsImagesRepository;

    private final DigitalOceanSpacesService digitalOceanSpacesService;

    private final DigitalOceanSpacesConfig config;

    @Override
    public PaginationResponse<NewsImagesGallery> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<NewsImagesGallery> newsImagesGalleryPage = newsImagesGalleryRepository.findAll(pageable);
        List<NewsImagesGallery> newsImagesGalleries = newsImagesGalleryPage.getContent();

        PaginationResponse<NewsImagesGallery> response = new PaginationResponse<>();
        response.setData(newsImagesGalleries);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All news-images-gallery fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(newsImagesGalleryPage.getNumber() + 1);
        meta.setFrom(newsImagesGalleryPage.getNumber() * newsImagesGalleryPage.getSize() + 1);
        meta.setLastPage(newsImagesGalleryPage.getTotalPages());
        meta.setPath("/news-images-gallery");
        meta.setPerPage(newsImagesGalleryPage.getSize());
        meta.setTo((int) newsImagesGalleryPage.getTotalElements());
        meta.setTotal((int) newsImagesGalleryPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/news-images-gallery?page=1");
        links.setLast("/news-images-gallery?page=" + newsImagesGalleryPage.getTotalPages());
        if (newsImagesGalleryPage.hasPrevious()) {
            links.setPrev("/news-images-gallery?page=" + newsImagesGalleryPage.previousPageable().getPageNumber());
        }
        if (newsImagesGalleryPage.hasNext()) {
            links.setNext("/news-images-gallery?page=" + newsImagesGalleryPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<NewsImagesGallery> getAllNewsImagesGallery() {

        return newsImagesGalleryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public NewsImagesGallery store(NewsImagesGalleryRequest request) {

        List<NewsImagesGallery> savedGalleries = new ArrayList<>();

        for (NewsImagesGalleryRequest.GalleryItem galleryItem : request.getNewsImageGallery()) {
            // Upload the image and get the URL
            String folderName = "news-images-galleries";
            MultipartFile imageFile = galleryItem.getImage();
            String objectKey = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            String imageUrl;
            try {
                imageUrl = digitalOceanSpacesService.uploadImage(imageFile, folderName, objectKey);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Create and save the new gallery item
            NewsImagesGallery gallery = new NewsImagesGallery();
            gallery.setNewsImageId(request.getNewsImageId());
            gallery.setTitleEn(galleryItem.getTitleEn());
            gallery.setTitleBn(galleryItem.getTitleBn());
            gallery.setSourceEn(galleryItem.getSourceEn());
            gallery.setSourceBn(galleryItem.getSourceBn());
            gallery.setImage(imageUrl);
            gallery.setCreatedBy(request.getCreatedBy());
            gallery.setStatus(request.isStatus());

            savedGalleries.add(newsImagesGalleryRepository.save(gallery));
        }

        return null;
    }

    @Override
    public NewsImagesGallery edit(Integer newsImageGalleryId) {

        return newsImagesGalleryRepository.findById(newsImageGalleryId).orElseThrow(() -> new RuntimeException("News images gallery id not found " + newsImageGalleryId));
    }

    @Override
    public NewsImagesGallery update(Integer newsImageGalleryId, NewsImagesGalleryRequest request) {

        // Fetch the existing NewsImagesGallery by its ID
        NewsImagesGallery existingGallery = newsImagesGalleryRepository.findById(newsImageGalleryId)
                .orElseThrow(() -> new GlobalException("News Image Gallery not found with id: " + newsImageGalleryId));

        // Update other fields first
        existingGallery.setNewsImageId(request.getNewsImageId());
        existingGallery.setCreatedBy(request.getCreatedBy());
        existingGallery.setStatus(request.isStatus());

        // Check if the new image gallery list is provided
        if (request.getNewsImageGallery() != null && !request.getNewsImageGallery().isEmpty()) {
            // Process the first gallery item for simplicity, assuming only one item
            NewsImagesGalleryRequest.GalleryItem galleryItem = request.getNewsImageGallery().get(0);

            // Validate the image file
            MultipartFile imageFile = galleryItem.getImage();

            if (imageFile != null && !imageFile.isEmpty()) {
                // Delete the old image from DigitalOcean Spaces if it exists
                if (existingGallery.getImage() != null) {
                    String oldImageUrl = existingGallery.getImage();
                    String oldImageKey = extractObjectKeyFromUrl(oldImageUrl);  // Extract the objectKey from the URL

                    // Call deleteImage to remove the old image from DigitalOcean Spaces
                    digitalOceanSpacesService.deleteImage(oldImageKey);
                }

                // Upload the new image
                String folderName = "news-images-galleries";
                String objectKey = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                String imageUrl;

                try {
                    imageUrl = digitalOceanSpacesService.uploadImage(imageFile, folderName, objectKey);
                    // Update image URL
                    existingGallery.setImage(imageUrl);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
                }
            } else {
                // If the image is null or empty, keep the old image
                System.out.println("No new image provided, keeping the old image: " + existingGallery.getImage());
            }

            // Update titles and sources if needed
            existingGallery.setTitleEn(galleryItem.getTitleEn());
            existingGallery.setTitleBn(galleryItem.getTitleBn());
            existingGallery.setSourceEn(galleryItem.getSourceEn());
            existingGallery.setSourceBn(galleryItem.getSourceBn());
        } else {
            System.out.println("No new image gallery provided, keeping current gallery data.");
        }

        // Save the updated NewsImagesGallery entity to the database
        return newsImagesGalleryRepository.save(existingGallery);
    }

    @Override
    public void destroy(Integer newsImageGalleryId) {

        newsImagesGalleryRepository.deleteById(newsImageGalleryId);

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
