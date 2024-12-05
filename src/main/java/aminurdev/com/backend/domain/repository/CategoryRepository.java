package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
