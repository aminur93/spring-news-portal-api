package aminurdev.com.backend.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class NewsLetterRequest {

    private Integer id;

    private String email;
}
