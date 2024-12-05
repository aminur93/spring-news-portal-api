package aminurdev.com.backend.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_en")
    private String name_en;

    @Column(name = "name_bn")
    private String name_bn;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_en")
    private String phone_en;

    @Column(name = "phone_bn")
    private String phone_bn;

    @Lob
    @Column(name = "address_en", columnDefinition = "LONGTEXT")
    private String address_en;

    @Lob
    @Column(name = "address_bn", columnDefinition = "LONGTEXT")
    private String address_bn;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "gender_en")
    private String gender_en;

    @Column(name = "gender_bn")
    private String gender_bn;

    @Lob
    @Column(name = "biography_en", columnDefinition = "LONGTEXT")
    private String biography_en;

    @Lob
    @Column(name = "biography_bn", columnDefinition = "LONGTEXT")
    private String biography_bn;

    @Column(name = "created_by")
    private Integer created_by;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = true)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP", nullable = true)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate()
    {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate()
    {
        updatedAt = LocalDateTime.now();
    }
}
