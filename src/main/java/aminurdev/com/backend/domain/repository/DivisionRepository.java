package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.Division;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DivisionRepository extends JpaRepository<Division, Integer> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM districts WHERE division_id = ?1", nativeQuery = true)
    void deleteDistrictsByDivisionId(Integer divisionId);
}
