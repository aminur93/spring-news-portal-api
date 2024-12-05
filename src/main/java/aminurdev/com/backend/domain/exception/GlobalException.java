package aminurdev.com.backend.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class GlobalException extends RuntimeException{


    // Constructor with message and default status (INTERNAL_SERVER_ERROR)
    public GlobalException(String message) {
        super(message);
    }

    // Constructor with message, cause, and custom status
    public GlobalException(String message, Throwable cause) {
        super(message, cause);
    }

}
