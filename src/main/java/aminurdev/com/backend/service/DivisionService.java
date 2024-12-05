package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.Division;
import aminurdev.com.backend.domain.request.DivisionRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface DivisionService {

    PaginationResponse<Division> index(Sort.Direction direction, int page, int perPage);

    List<Division> getAllDivisions();

    Division store(DivisionRequest request);

    Division edit(Integer divisionId);

    Division update(DivisionRequest request, Integer divisionId);

    void destroy(Integer divisionId);

}
