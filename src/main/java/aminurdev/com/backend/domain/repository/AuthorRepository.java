package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
