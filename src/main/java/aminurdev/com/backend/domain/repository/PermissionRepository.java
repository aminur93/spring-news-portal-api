package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {


    @Query(value = "SELECT p.title_en, JSON_ARRAYAGG(JSON_OBJECT('id', p.id, 'name_en', p.name_en, 'name_bn', p.name_bn)) AS data " +
            "FROM permissions p " +
            "GROUP BY p.title_en", nativeQuery = true)
    List<Map<String, Object>> findPermissionsGroupedByTitle();
}
