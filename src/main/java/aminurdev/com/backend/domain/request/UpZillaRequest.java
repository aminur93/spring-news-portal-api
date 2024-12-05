package aminurdev.com.backend.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class UpZillaRequest {

    private Integer id;

    @NotNull(message = "District ID is required")
    private Integer districtId;

    private String name_en;

    private String name_bn;

    private Boolean status = false;

    private Integer createdBy;

}
