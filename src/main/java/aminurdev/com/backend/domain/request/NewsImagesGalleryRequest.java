package aminurdev.com.backend.domain.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class NewsImagesGalleryRequest {


    private Integer newsImageId;
    private List<@Valid GalleryItem> newsImageGallery; // This should match the incoming request structure
    private Integer createdBy;
    private boolean status;

    @Data
    public static class GalleryItem {

        private String titleEn;

        private String titleBn;

        private String sourceEn;

        private String sourceBn;

        private MultipartFile image;
    }
}
