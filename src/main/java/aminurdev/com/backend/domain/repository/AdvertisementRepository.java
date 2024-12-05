package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
}
