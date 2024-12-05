package aminurdev.com.backend.domain.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class NewsVideoGalleryRequest {

    private Integer newsImageId;

    private List<@Valid VideoGalleryItem> newsVideosGallery;

    private Integer createdBy;

    private boolean status;

    @Data
    public static class VideoGalleryItem{

        private String titleEn;

        private String titleBn;

        private String sourceEn;

        private String sourceBn;

        private MultipartFile video;
    }
}
