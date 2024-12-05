package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.Category;
import aminurdev.com.backend.domain.entity.City;
import aminurdev.com.backend.domain.entity.Comment;
import aminurdev.com.backend.domain.entity.News;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.CommentRepository;
import aminurdev.com.backend.domain.repository.NewsRepository;
import aminurdev.com.backend.domain.request.CommentRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.CommentService;
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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final NewsRepository newsRepository;

    private final DigitalOceanSpacesService digitalOceanSpacesService;

    private final DigitalOceanSpacesConfig config;

    @Override
    public PaginationResponse<Comment> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<Comment> commentPage = commentRepository.findAll(pageable);
        List<Comment> comments = commentPage.getContent();

        PaginationResponse<Comment> response = new PaginationResponse<>();
        response.setData(comments);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All comments fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(commentPage.getNumber() + 1);
        meta.setFrom(commentPage.getNumber() * commentPage.getSize() + 1);
        meta.setLastPage(commentPage.getTotalPages());
        meta.setPath("/comment");
        meta.setPerPage(commentPage.getSize());
        meta.setTo((int) commentPage.getTotalElements());
        meta.setTotal((int) commentPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/comment?page=1");
        links.setLast("/comment?page=" + commentPage.getTotalPages());
        if (commentPage.hasPrevious()) {
            links.setPrev("/comment?page=" + commentPage.previousPageable().getPageNumber());
        }
        if (commentPage.hasNext()) {
            links.setNext("/comment?page=" + commentPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<Comment> getAllComments() {

        return commentRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Comment store(CommentRequest request) {

        try {

            Comment comment = new Comment();

            News news = newsRepository.findById(request.getNewsId()).orElseThrow(() -> new RuntimeException("News is not found by this id : " + request.getNewsId()));

            comment.setNews(news);
            comment.setName(request.getName());
            comment.setEmail(request.getEmail());
            comment.setComment(request.getComment());

            // Handle icon upload
            String imageUrl = null;
            if (request.getImage() != null) { // Assuming request.getIcon() returns a MultipartFile
                MultipartFile imageFile = request.getImage(); // Get the MultipartFile
                String folderName = "comment_image";  // Specify your desired folder name
                String objectKey = imageFile.getOriginalFilename(); // Use the original filename or generate a unique name

                // Call uploadImage method from DigitalOceanSpacesService
                imageUrl = digitalOceanSpacesService.uploadImage(imageFile, folderName, objectKey);
            }

            comment.setImage(imageUrl);

            return commentRepository.save(comment);

        }catch (Exception exception){

            throw new GlobalException("Error while storing comment : " + exception.getMessage(), exception);
        }
    }

    @Override
    public Comment edit(Integer commentId) {

        return commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment id is not found"));
    }

    @Override
    public Comment update(Integer commentId, CommentRequest request) {

        try {

            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment id is not found"));

            News news = newsRepository.findById(request.getNewsId()).orElseThrow(() -> new RuntimeException("News is not found by this id : " + request.getNewsId()));

            comment.setNews(news);
            comment.setName(request.getName());
            comment.setEmail(request.getEmail());
            comment.setComment(request.getComment());

            // Check if a new icon is provided
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                // Delete the old icon from DigitalOcean Spaces if it exists
                if (comment.getImage() != null) {
                    String oldIconUrl = comment.getImage();
                    String oldIconKey = extractObjectKeyFromUrl(oldIconUrl);  // Extract the objectKey from the URL

                    // Call deleteIcon to remove the old icon
                    digitalOceanSpacesService.deleteImage(oldIconKey);
                }

                // Upload the new icon
                String folderName = "comment_image";
                String objectKey = request.getImage().getOriginalFilename(); // Use the original filename or generate a unique one

                // Upload the new icon and get the new icon URL
                String newImageUrl = digitalOceanSpacesService.uploadImage(request.getImage(), folderName, objectKey);

                // Update the category with the new icon URL
                comment.setImage(newImageUrl);
            }

            return commentRepository.save(comment);

        }catch (Exception exception){

            throw new GlobalException("Error while storing comment : " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer commentId) {

        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment id is not found"));

            // Extract objectKey from the icon URL (assuming the icon is stored as a full URL)
            String iconUrl = comment.getImage();
            String objectKey = extractObjectKeyFromUrl(iconUrl); // Implement this method to extract the objectKey

            // Delete the icon from DigitalOcean Spaces
            digitalOceanSpacesService.deleteImage(objectKey);

            commentRepository.delete(comment);

        } catch (Exception exception) {

            throw new GlobalException("Error while deleting comment : " + exception.getMessage(), exception);
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
