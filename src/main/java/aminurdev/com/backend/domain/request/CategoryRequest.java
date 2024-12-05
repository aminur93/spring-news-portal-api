package aminurdev.com.backend.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class CategoryRequest {
    private Integer id;

    private String name_en;
    private String name_bn;

    private String description_en;
    private String description_bn;


    private MultipartFile icon;

    private Boolean status = false;

    private Integer createdBy;
}
