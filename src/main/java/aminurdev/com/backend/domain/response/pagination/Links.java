package aminurdev.com.backend.domain.response.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Links {

    private String first;
    private String last;
    private String prev;
    private String next;
}
