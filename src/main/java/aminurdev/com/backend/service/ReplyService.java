package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.Reply;
import aminurdev.com.backend.domain.request.ReplyRequest;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ReplyService {

    PaginationResponse<Reply> index(Sort.Direction direction, int page, int perPage);

    List<Reply> getAllReplies();

    Reply store(ReplyRequest request);

    Reply edit(Integer replyId);

    Reply update(Integer replyId, ReplyRequest request);

    void destroy(Integer replyId);
}
