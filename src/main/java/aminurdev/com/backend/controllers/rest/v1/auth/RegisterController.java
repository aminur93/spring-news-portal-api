package aminurdev.com.backend.controllers.rest.v1.auth;

import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.request.UserRequest;
import aminurdev.com.backend.domain.response.GlobalResponse;
import aminurdev.com.backend.service.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Register Api", description = "Operations related to user management such as creating, updating, and deleting users.")
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("/register")
    @Operation(
            summary = "User Registration",
            description = "This API endpoint allows users to register by providing necessary information such as name, email, and password. It validates the input data and, upon successful registration, returns a confirmation message along with user details. Optionally, email verification may be sent."
    )
    public ResponseEntity<GlobalResponse> register(@Valid @RequestBody UserRequest request)
    {
        try {

            User user = registerService.register(request);

            GlobalResponse globalResponse = new GlobalResponse().success(
                    user,
                    "Register successful",
                    true,
                    HttpStatus.CREATED.value()
            );

            return ResponseEntity.status(HttpStatus.CREATED.value()).body(globalResponse);

        }catch (Exception exception){

            GlobalResponse globalResponse = new GlobalResponse().error(
                    Collections.singletonList(exception.getMessage()),
                    "Failed server error",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );

            return ResponseEntity.ok(globalResponse);
        }
    }
}
