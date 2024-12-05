package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Integer> {
}
