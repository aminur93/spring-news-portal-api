package aminurdev.com.backend.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class NewsRequest {

    private Integer categoryId;

    private String headingEn;

    private String headingBn;

    private String titleEn;

    private String titleBn;

    private String descriptionEn;

    private String descriptionBn;

    private String additionalDescriptionEn;

    private String additionalDescriptionBn;

    private MultipartFile coverImage;

    private MultipartFile image;

    private LocalDate date;

    private String sourceEn;

    private String sourceBn;

    private Integer newsColumn;

    private Boolean isPopular;

    private Boolean isBreaking;

    private Boolean isOpinion;

    private Boolean isForYou;

    private Boolean isDiscussed;

    private Boolean isGoodNews;

    private Boolean isBd;

    private Boolean isWorld;

    private Boolean isTop;

    private Boolean isMiddle;

    private Boolean isBottom;

    private Boolean isFeatured;

    private Boolean isTrending;

    private Boolean isFashion;

    private Boolean isCartoon;

    private Integer count;

    private Integer status; // pending, rejected, approved

    private Integer createdBy;

    private Integer countryId;

    private Integer cityId;

    private Integer divisionId;

    private Integer districtId;

    private Integer upzillaId;

    private List<Integer> subCategoryIds;

    private List<Integer> tagIds;
}
