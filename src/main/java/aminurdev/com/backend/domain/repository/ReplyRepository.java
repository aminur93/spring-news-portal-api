package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
}
