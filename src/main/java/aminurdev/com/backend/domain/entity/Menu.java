package aminurdev.com.backend.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = true)
    private Permission permission;

    @Column(name = "parent_id")
    private Integer parent_id;

    @Column(name = "name_en")
    private String name_en;

    @Column(name = "name_bn")
    private String name_bn;

    @Column(name = "url")
    private String url;

    @Column(name = "icon")
    private String icon;

    @Column(name = "header_menu", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean headerMenu = false;

    @Column(name = "sidebar_menu", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean sidebarMenu = false;

    @Column(name = "dropdown_menu", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean dropdownMenu = false;

    @Column(name = "children_parent_menu")
    private Integer childrenParentMenu;

    @Column(name = "status", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean status = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

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

    @Transient
    private List<Menu> children = new ArrayList<>();
}
