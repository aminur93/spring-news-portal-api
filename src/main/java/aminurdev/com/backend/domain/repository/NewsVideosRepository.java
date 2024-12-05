package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.NewsVideo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsVideosRepository extends JpaRepository<NewsVideo, Integer> {
}
