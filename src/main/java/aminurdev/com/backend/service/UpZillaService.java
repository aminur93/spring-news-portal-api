package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.UpZilla;
import aminurdev.com.backend.domain.request.UpZillaRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UpZillaService {

    PaginationResponse<UpZilla> index(Sort.Direction direction, int page, int perPage);

    List<UpZilla> getAllUpZillas();

    UpZilla store(UpZillaRequest request);

    UpZilla edit(Integer upzillaId);

    UpZilla update(UpZillaRequest request, Integer upzillaId);

    void destroy(Integer upzillaId);
}
