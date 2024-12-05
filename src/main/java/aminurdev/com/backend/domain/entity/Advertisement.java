package aminurdev.com.backend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "advertisements")
public class Advertisement {

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

    @Lob
    @Column(name = "description_en", columnDefinition = "LONGTEXT", nullable = true)
    private String description_en;

    @Lob
    @Column(name = "description_bn", columnDefinition = "LONGTEXT", nullable = true)
    private String description_bn;

    @Column(name = "company_name_en")
    private String company_name_en;

    @Column(name = "company_name_bn")
    private String company_name_bn;

    @Column(name = "start_date", nullable = true)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = true)
    private LocalDateTime endDate;

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
