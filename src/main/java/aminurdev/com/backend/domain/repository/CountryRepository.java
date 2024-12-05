package aminurdev.com.backend.domain.repository;

import aminurdev.com.backend.domain.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import jakarta.transaction.Transactional;

public interface CountryRepository extends JpaRepository<Country, Integer>{

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cities WHERE country_id = ?1", nativeQuery = true)
    void deleteCitiesByCountryId(Integer countryId);


}
