package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM model_has_role WHERE user_id = ?1", nativeQuery = true)
    void deleteUserRoles(Integer userId);
}
