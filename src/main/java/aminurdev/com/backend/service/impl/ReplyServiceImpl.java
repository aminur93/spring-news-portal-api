package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.Comment;
import aminurdev.com.backend.domain.entity.Reply;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.CommentRepository;
import aminurdev.com.backend.domain.repository.ReplyRepository;
import aminurdev.com.backend.domain.request.ReplyRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.DigitalOceanSpacesService;
import aminurdev.com.backend.service.ReplyService;
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
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    private final CommentRepository commentRepository;

    private final DigitalOceanSpacesService digitalOceanSpacesService;

    private final DigitalOceanSpacesConfig config;

    @Override
    public PaginationResponse<Reply> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        Page<Reply> replyPage = replyRepository.findAll(pageable);
        List<Reply> replies = replyPage.getContent();

        PaginationResponse<Reply> response = new PaginationResponse<>();
        response.setData(replies);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All reply fetched successfully");

        Meta meta = new Meta();
        meta.setCurrentPage(replyPage.getNumber() + 1);
        meta.setFrom(replyPage.getNumber() * replyPage.getSize() + 1);
        meta.setLastPage(replyPage.getTotalPages());
        meta.setPath("/reply");
        meta.setPerPage(replyPage.getSize());
        meta.setTo((int) replyPage.getTotalElements());
        meta.setTotal((int) replyPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();
        links.setFirst("/reply?page=1");
        links.setLast("/reply?page=" + replyPage.getTotalPages());
        if (replyPage.hasPrevious()) {
            links.setPrev("/reply?page=" + replyPage.previousPageable().getPageNumber());
        }
        if (replyPage.hasNext()) {
            links.setNext("/reply?page=" + replyPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<Reply> getAllReplies() {

        return replyRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Reply store(ReplyRequest request) {

        try {

            Reply reply = new Reply();

            Comment comment = commentRepository.findById(request.getCommentId()).orElseThrow(() -> new RuntimeException("Comment id is not found"));

            reply.setComment(comment);
            reply.setName(request.getName());
            reply.setEmail(request.getEmail());

            // Handle icon upload
            String imageUrl = null;
            if (request.getImage() != null) { // Assuming request.getIcon() returns a MultipartFile
                MultipartFile imageFile = request.getImage(); // Get the MultipartFile
                String folderName = "reply_image";  // Specify your desired folder name
                String objectKey = imageFile.getOriginalFilename(); // Use the original filename or generate a unique name

                // Call uploadImage method from DigitalOceanSpacesService
                imageUrl = digitalOceanSpacesService.uploadImage(imageFile, folderName, objectKey);
            }

            reply.setImage(imageUrl);
            reply.setReply(request.getReply());

            return replyRepository.save(reply);

        }catch (Exception exception){

            throw new GlobalException("Error while storing reply : " + exception.getMessage(), exception);
        }
    }

    @Override
    public Reply edit(Integer replyId) {

        return replyRepository.findById(replyId).orElseThrow(() -> new RuntimeException("Reply id is not found"));
    }

    @Override
    public Reply update(Integer replyId, ReplyRequest request) {

        try {

            Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new RuntimeException("Reply id is not found"));

            Comment comment = commentRepository.findById(request.getCommentId()).orElseThrow(() -> new RuntimeException("Comment id is not found"));

            reply.setComment(comment);
            reply.setName(request.getName());
            reply.setEmail(request.getEmail());

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
                String folderName = "reply_image";
                String objectKey = request.getImage().getOriginalFilename(); // Use the original filename or generate a unique one

                // Upload the new icon and get the new icon URL
                String newImageUrl = digitalOceanSpacesService.uploadImage(request.getImage(), folderName, objectKey);

                // Update the category with the new icon URL
                reply.setImage(newImageUrl);
            }

            reply.setReply(request.getReply());

            return replyRepository.save(reply);

        }catch (Exception exception){

            throw new GlobalException("Error while update reply : " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer replyId) {

        try {
            Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new RuntimeException("Reply id is not found"));

            // Extract objectKey from the icon URL (assuming the icon is stored as a full URL)
            String iconUrl = reply.getImage();
            String objectKey = extractObjectKeyFromUrl(iconUrl); // Implement this method to extract the objectKey

            // Delete the icon from DigitalOcean Spaces
            digitalOceanSpacesService.deleteImage(objectKey);

            replyRepository.delete(reply);

        } catch (Exception exception) {

            throw new GlobalException("Error while deleting reply : " + exception.getMessage(), exception);
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
