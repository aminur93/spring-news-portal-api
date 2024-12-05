package aminurdev.com.backend.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class SubCategoryRequest {

    private Integer id;

    private Integer category_id;

    private String name_en;
    private String name_bn;

    private String description_en;
    private String description_bn;


    private Boolean status = false;

    private Integer createdBy;
}
