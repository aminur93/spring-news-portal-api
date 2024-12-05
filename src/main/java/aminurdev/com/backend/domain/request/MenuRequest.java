package aminurdev.com.backend.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class MenuRequest {

    private Integer id;

    private Integer permission_id;

    private int parent_id;

    private String name_en;

    private String name_bn;

    private String url;

    private String icon;

    private boolean headerMenu;

    private boolean sidebarMenu;

    private boolean dropdownMenu;

    private Integer childrenParentMenu;

    private boolean status;
}
