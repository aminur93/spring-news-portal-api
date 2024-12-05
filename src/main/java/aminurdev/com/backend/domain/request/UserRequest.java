package aminurdev.com.backend.domain.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class UserRequest {

    private Integer id;

    private String name_en;

    private String name_bn;

    @NotBlank(message = "Email must not be blank")
    @NotNull(message = "Email field is required")
    @Email(message = "Email should be valid")
    private String email;


    private String phone_en;

    private String phone_bn;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character (@#$%^&+=)"
    )
    private String password;

    @NotNull(message = "Role cannot be null")
    private Integer role;
}
