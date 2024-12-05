package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.NewsLetter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsLetterRepository extends JpaRepository<NewsLetter, Integer> {
}
