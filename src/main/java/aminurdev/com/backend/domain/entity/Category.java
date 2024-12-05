package aminurdev.com.backend.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_en")
    private String name_en;

    @Column(name = "name_bn")
    private String name_bn;

    @Lob
    @Column(name = "description_en", columnDefinition = "LONGTEXT", nullable = true)
    private String description_en;

    @Lob
    @Column(name = "description_bn", columnDefinition = "LONGTEXT", nullable = true)
    private String description_bn;

    @Column(name = "icon")
    private String icon;

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

    @OneToMany(mappedBy = "category")
    @JsonIncludeProperties({"id","category_id", "title_en", "title_bn", "description_en", "description_en", "status", "created_at", "updated_at"})
    @JsonManagedReference
    @JsonBackReference
    private List<SubCategory> subCategories;
}
