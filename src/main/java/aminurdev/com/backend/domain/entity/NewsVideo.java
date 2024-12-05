package aminurdev.com.backend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "news_videos")
public class NewsVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title_en")
    private String title_en;

    @Column(name = "title_bn")
    private String title_bn;

    @Column(name = "slogan_en")
    private String slogan_en;

    @Column(name = "slogan_bn")
    private String slogan_bn;

    @Column(name = "description_en")
    private String description_en;

    @Column(name = "description_bn")
    private String description_bn;

    @Column(name = "video")
    private String video;

    @Column(name = "status", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean status = false;

    @JoinColumn(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = true)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP", nullable = true)
    private LocalDateTime updatedAt;

    // One-to-many with JoinColumn for Category
    @ManyToMany
    @JoinTable(
            name = "news_videos_categories", // Your existing table
            joinColumns = @JoinColumn(name = "news_video_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    // One-to-many with JoinColumn for SubCategory
    @ManyToMany
    @JoinTable(
            name = "news_videos_sub_categories", // Your existing table
            joinColumns = @JoinColumn(name = "news_video_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_category_id")
    )
    private List<SubCategory> subCategories;

    // One-to-many with JoinColumn for TagEntity
    @ManyToMany
    @JoinTable(
            name = "news_videos_tag", // Your existing table
            joinColumns = @JoinColumn(name = "news_video_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<TagEntity> tagEntities;

    @PreRemove
    private void preRemove() {
        this.categories.clear();
        this.subCategories.clear();
        this.tagEntities.clear();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
