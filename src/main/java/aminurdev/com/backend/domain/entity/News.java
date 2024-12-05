package aminurdev.com.backend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "heading_en")
    private String headingEn;

    @Column(name = "heading_bn")
    private String headingBn;

    @Column(name = "title_en")
    private String titleEn;

    @Column(name = "title_bn")
    private String titleBn;

    @Lob
    @Column(name = "description_en", columnDefinition = "LONGTEXT")
    private String descriptionEn;

    @Lob
    @Column(name = "description_bn", columnDefinition = "LONGTEXT")
    private String descriptionBn;

    @Lob
    @Column(name = "additional_description_en", columnDefinition = "LONGTEXT")
    private String additionalDescriptionEn;

    @Lob
    @Column(name = "additional_description_bn", columnDefinition = "LONGTEXT")
    private String additionalDescriptionBn;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "image")
    private String image;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "source_en")
    private String sourceEn;

    @Column(name = "source_bn")
    private String sourceBn;

    @Column(name = "news_column")
    private Integer newsColumn;

    @Column(name = "is_popular", columnDefinition = "TINYINT(1)")
    private Boolean isPopular;

    @Column(name = "is_breaking", columnDefinition = "TINYINT(1)")
    private Boolean isBreaking;

    @Column(name = "is_opinion", columnDefinition = "TINYINT(1)")
    private Boolean isOpinion;

    @Column(name = "is_for_you", columnDefinition = "TINYINT(1)")
    private Boolean isForYou;

    @Column(name = "is_discussed", columnDefinition = "TINYINT(1)")
    private Boolean isDiscussed;

    @Column(name = "is_good_news", columnDefinition = "TINYINT(1)")
    private Boolean isGoodNews;

    @Column(name = "is_bd", columnDefinition = "TINYINT(1)")
    private Boolean isBd;

    @Column(name = "is_world", columnDefinition = "TINYINT(1)")
    private Boolean isWorld;

    @Column(name = "is_top", columnDefinition = "TINYINT(1)")
    private Boolean isTop;

    @Column(name = "is_middle", columnDefinition = "TINYINT(1)")
    private Boolean isMiddle;

    @Column(name = "is_bottom", columnDefinition = "TINYINT(1)")
    private Boolean isBottom;

    @Column(name = "is_featured", columnDefinition = "TINYINT(1)")
    private Boolean isFeatured;

    @Column(name = "is_trending", columnDefinition = "TINYINT(1)")
    private Boolean isTrending;

    @Column(name = "is_fashion", columnDefinition = "TINYINT(1)")
    private Boolean isFashion;

    @Column(name = "is_cartoon", columnDefinition = "TINYINT(1)")
    private Boolean isCartoon;

    @Column(name = "count")
    private Integer count;

    @Column(name = "status", columnDefinition = "TINYINT")
    private Integer status; // pending, rejected, approved

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "country_id", insertable = false, updatable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "city_id", insertable = false, updatable = false)
    private City city;

    @ManyToOne
    @JoinColumn(name = "division_id", insertable = false, updatable = false)
    private Division division;

    @ManyToOne
    @JoinColumn(name = "district_id", insertable = false, updatable = false)
    private District district;

    @ManyToOne
    @JoinColumn(name = "upzilla_id", insertable = false, updatable = false)
    private UpZilla upzilla;

    @ManyToMany
    @JoinTable(
            name = "news_sub_categories", // Your existing table
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_category_id")
    )
    private List<SubCategory> subCategories;

    @ManyToMany
    @JoinTable(
            name = "news_tags", // Your existing table
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<TagEntity> tagEntities;

    // Auto-update timestamps
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
