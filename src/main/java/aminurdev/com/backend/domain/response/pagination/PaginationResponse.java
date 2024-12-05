package aminurdev.com.backend.domain.response.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {

    private List<T> data;
    private Links links;
    private Meta meta;
    private String message;
    private boolean success;
    private Integer statusCode;
}
