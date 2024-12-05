package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.NewsImagesGallery;
import aminurdev.com.backend.domain.entity.NewsVideoGallery;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.NewsVideoGalleryRepository;
import aminurdev.com.backend.domain.request.NewsImagesGalleryRequest;
import aminurdev.com.backend.domain.request.NewsVideoGalleryRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.DigitalOceanSpacesService;
import aminurdev.com.backend.service.NewsImagesGalleryService;
import aminurdev.com.backend.service.NewsVideoGalleryService;
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
public class NewsVideoGalleryServiceImpl implements NewsVideoGalleryService {

    private final NewsVideoGalleryRepository newsVideoGalleryRepository;

    private final DigitalOceanSpacesService digitalOceanSpacesService;

    private final DigitalOceanSpacesConfig config;


    @Override
    public PaginationResponse<NewsVideoGallery> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<NewsVideoGallery> newsVideoGalleryPage = newsVideoGalleryRepository.findAll(pageable);
        List<NewsVideoGallery> newsImagesGalleries = newsVideoGalleryPage.getContent();

        PaginationResponse<NewsVideoGallery> response = new PaginationResponse<>();
        response.setData(newsImagesGalleries);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All news-videos-gallery fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(newsVideoGalleryPage.getNumber() + 1);
        meta.setFrom(newsVideoGalleryPage.getNumber() * newsVideoGalleryPage.getSize() + 1);
        meta.setLastPage(newsVideoGalleryPage.getTotalPages());
        meta.setPath("/news-videos-gallery");
        meta.setPerPage(newsVideoGalleryPage.getSize());
        meta.setTo((int) newsVideoGalleryPage.getTotalElements());
        meta.setTotal((int) newsVideoGalleryPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/news-videos-gallery?page=1");
        links.setLast("/news-videos-gallery?page=" + newsVideoGalleryPage.getTotalPages());
        if (newsVideoGalleryPage.hasPrevious()) {
            links.setPrev("/news-videos-gallery?page=" + newsVideoGalleryPage.previousPageable().getPageNumber());
        }
        if (newsVideoGalleryPage.hasNext()) {
            links.setNext("/news-videos-gallery?page=" + newsVideoGalleryPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<NewsVideoGallery> getAllNewsVideosGallery() {

        return newsVideoGalleryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public NewsVideoGallery store(NewsVideoGalleryRequest request) {

        List<NewsVideoGallery> savedGalleries = new ArrayList<>();

        for (NewsVideoGalleryRequest.VideoGalleryItem galleryItem : request.getNewsVideosGallery()) {
            // Upload the image and get the URL
            String folderName = "news-videos-galleries";
            MultipartFile imageFile = galleryItem.getVideo();
            String objectKey = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            String videoUrl;
            try {
                videoUrl = digitalOceanSpacesService.uploadImage(imageFile, folderName, objectKey);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Create and save the new gallery item
            NewsVideoGallery gallery = new NewsVideoGallery();
            gallery.setNewsImageId(request.getNewsImageId());
            gallery.setTitleEn(galleryItem.getTitleEn());
            gallery.setTitleBn(galleryItem.getTitleBn());
            gallery.setSourceEn(galleryItem.getSourceEn());
            gallery.setSourceBn(galleryItem.getSourceBn());
            gallery.setVideo(videoUrl);
            gallery.setCreatedBy(request.getCreatedBy());
            gallery.setStatus(request.isStatus());

            savedGalleries.add(newsVideoGalleryRepository.save(gallery));
        }

        return null;
    }


    @Override
    public NewsVideoGallery edit(Integer newsVideoGalleryId) {

        return newsVideoGalleryRepository.findById(newsVideoGalleryId).orElseThrow(() -> new RuntimeException("News video gallery id not found"));
    }

    @Override
    public NewsVideoGallery update(Integer newsVideoGalleryId, NewsVideoGalleryRequest request) {

        // Fetch the existing NewsVideoGallery by its ID
        NewsVideoGallery existingGallery = newsVideoGalleryRepository.findById(newsVideoGalleryId)
                .orElseThrow(() -> new GlobalException("News Image Gallery not found with id: " + newsVideoGalleryId));

        // Update other fields first
        existingGallery.setNewsImageId(request.getNewsImageId());
        existingGallery.setCreatedBy(request.getCreatedBy());
        existingGallery.setStatus(request.isStatus());

        // Check if the new video gallery list is provided
        if (request.getNewsVideosGallery() != null && !request.getNewsVideosGallery().isEmpty()) {

            // Process the first gallery item for simplicity, assuming only one item
            NewsVideoGalleryRequest.VideoGalleryItem videoGalleryItem = request.getNewsVideosGallery().get(0);

            // Validate the video file
            MultipartFile videoFile = videoGalleryItem.getVideo();

            if (videoFile != null && !videoFile.isEmpty()) {
                // Delete the old video from DigitalOcean Spaces if it exists
                if (existingGallery.getVideo() != null) {
                    String oldVideoUrl = existingGallery.getVideo();
                    String oldVideoKey = extractObjectKeyFromUrl(oldVideoUrl);  // Extract the objectKey from the URL

                    // Call deleteImage to remove the old video from DigitalOcean Spaces
                    digitalOceanSpacesService.deleteImage(oldVideoKey);
                }

                // Upload the new video
                String folderName = "news-video-galleries";
                String objectKey = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
                String videoUrl;

                try {
                    videoUrl = digitalOceanSpacesService.uploadImage(videoFile, folderName, objectKey);
                    // Update video URL
                    existingGallery.setVideo(videoUrl);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload video: " + e.getMessage(), e);
                }
            } else {
                // If the video is null or empty, keep the old video
                System.out.println("No new video provided, keeping the old image: " + existingGallery.getVideo());
            }

            // Update titles and sources if needed
            existingGallery.setTitleEn(videoGalleryItem.getTitleEn());
            existingGallery.setTitleBn(videoGalleryItem.getTitleBn());
            existingGallery.setSourceEn(videoGalleryItem.getSourceEn());
            existingGallery.setSourceBn(videoGalleryItem.getSourceBn());
        } else {
            System.out.println("No new video gallery provided, keeping current gallery data.");
        }

        // Save the updated NewsVideoGallery entity to the database
        return newsVideoGalleryRepository.save(existingGallery);
    }

    @Override
    public void destroy(Integer newsVideoGalleryId) {

        newsVideoGalleryRepository.deleteById(newsVideoGalleryId);

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
