package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.City;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CityRepository extends JpaRepository<City, Integer> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM divisions WHERE city_id = ?1", nativeQuery = true)
    void deleteDivisionsByCityId(Integer cityId);
}
