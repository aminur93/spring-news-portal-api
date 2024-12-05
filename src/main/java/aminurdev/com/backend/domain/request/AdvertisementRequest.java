package aminurdev.com.backend.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class AdvertisementRequest {

    private Integer id;

    @NotBlank(message = "Title must not be blank")
    @NotNull(message = "Title is required")
    private String title_en;

    private String title_bn;

    private String slogan_en;

    private String slogan_bn;

    private String description_en;

    private String description_bn;

    private String company_name_en;
    private String company_name_bn;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private MultipartFile image;

    private Boolean status = false;

    private Integer createdBy;
}
