package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
