package aminurdev.com.backend.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {

    private Map<String, Object> errors;
    private String message;
    private Integer statusCode;

    public ValidationErrorResponse(Map<String, Object> errors)
    {
        this.errors = errors;
    }
}
