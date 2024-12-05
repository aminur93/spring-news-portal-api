package aminurdev.com.backend.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResponse {

    private List<Object> errors;
    private Object data;
    private String message;
    private boolean success;
    private Integer status;

    public GlobalResponse success(Object data, String message, boolean success, Integer status)
    {
        this.data = data;
        this.message = message;
        this.success = success;
        this.status = status;

        return this;
    }

    public GlobalResponse error(List<Object> errors, String message, boolean success, Integer status)
    {
        this.errors = errors;
        this.message = message;
        this.success = success;
        this.status = status;

        return this;
    }
}
