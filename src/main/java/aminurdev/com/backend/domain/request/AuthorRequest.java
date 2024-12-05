package aminurdev.com.backend.domain.request;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class AuthorRequest {

    private Integer id;

    private String name_en;
    private String name_bn;

    @NotNull(message = "email field is required")
    @Email(message = "email should be valid")
    private String email;

    @Size(min=11, max=11, message = "Phone must be 11 characters")
    private String phone_en;

    @Size(min=11, max=11, message = "Phone must be 11 characters")
    private String phone_bn;

    @Lob
    private String address_en;

    @Lob
    private String address_bn;

    @NotNull(message = "Date of birth field is required")
    private LocalDate dob;

    private String gender_en;
    private String gender_bn;

    @Lob
    private String biography_en;

    @Lob
    private String biography_bn;

    private Integer created_by;
}
