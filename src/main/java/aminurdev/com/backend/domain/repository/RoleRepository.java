package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM role_has_permission WHERE role_id = ?1", nativeQuery = true)
    void deleteRolePermissions(Integer roleId);
}
