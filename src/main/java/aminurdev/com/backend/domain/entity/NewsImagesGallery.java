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
@Table(name = "news_images_galleries")
public class NewsImagesGallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "news_image_id")
    private Integer newsImageId; // Updated to camelCase

    @Column(name = "title_en")
    private String titleEn; // Updated to camelCase

    @Column(name = "title_bn")
    private String titleBn; // Updated to camelCase

    @Column(name = "source_en")
    private String sourceEn; // Updated to camelCase

    @Column(name = "source_bn")
    private String sourceBn; // Updated to camelCase

    @Column(name = "image")
    private String image;

    @Column(name = "status", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean status = false;

    @JoinColumn(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = true)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP", nullable = true)
    private LocalDateTime updatedAt;

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
