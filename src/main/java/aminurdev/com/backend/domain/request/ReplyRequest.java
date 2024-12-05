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
public class ReplyRequest {

    private Integer id;

    private Integer commentId;

    private String name;

    private String email;

    private MultipartFile image;

    @Lob
    @NotNull(message = "Reply field is required")
    private String reply;
}
