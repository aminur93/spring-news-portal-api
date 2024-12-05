package aminurdev.com.backend.domain.request;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class CommentRequest {

    private Integer id;

    private Integer newsId;

    private String name;

    private String email;

    private MultipartFile image;

    @Lob
    @NotNull(message = "Comment field is required")
    private String comment;
}
