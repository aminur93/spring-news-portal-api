package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.District;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DistrictRepository extends JpaRepository<District, Integer> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM zillas WHERE district_id = ?1", nativeQuery = true)
    void deleteZillasByDistrictId(Integer districtId);

}
