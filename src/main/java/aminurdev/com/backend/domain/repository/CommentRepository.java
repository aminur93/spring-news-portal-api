package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
