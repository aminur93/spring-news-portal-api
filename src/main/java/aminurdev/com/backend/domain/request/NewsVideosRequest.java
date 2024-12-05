package aminurdev.com.backend.domain.request;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class NewsVideosRequest {

    private Integer id;

    private String title_en;
    private String title_bn;

    private String slogan_en;
    private String slogan_bn;

    @Lob
    private String description_en;

    @Lob
    private String description_bn;

    private MultipartFile video;

    private boolean status;

    private List<Integer> categoriesId;

    private List<Integer> subCategoriesId;

    private List<Integer> tagsId;
}
