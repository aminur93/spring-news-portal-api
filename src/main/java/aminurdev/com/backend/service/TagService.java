package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.TagEntity;
import aminurdev.com.backend.domain.request.TagRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TagService {

    PaginationResponse<TagEntity> index(Sort.Direction direction, int page, int perPage);

    List<TagEntity> getAllTags();

    TagEntity store(TagRequest request);

    TagEntity edit(Integer tagId);

    TagEntity update(TagRequest request, Integer tagId);

    void destroy(Integer tagId);
}
