package aminurdev.com.backend.controllers.rest.v1.auth;

import aminurdev.com.backend.domain.request.LoginRequest;
import aminurdev.com.backend.domain.request.TokenRequest;
import aminurdev.com.backend.domain.response.AuthResponse;
import aminurdev.com.backend.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Login Api", description = "Operations related to managing")
public class LoginController {

    private final LoginService loginService;

    private final LogoutHandler logoutHandler;

    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "This API endpoint allows users to log in by providing their credentials. It accepts a username or email and password, then validates the provided information. Upon successful authentication, a JWT token is returned which can be used to authorize subsequent requests to secured endpoints."
    )
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(loginService.login(request));
    }

    @PostMapping("/refresh-token")
    @Operation(
            summary = "Refresh Access Token",
            description = "This API endpoint allows users to refresh their access token using a valid refresh token. It checks the validity of the provided refresh token, and if valid, issues a new access token. This is useful when the current access token has expired and the user needs to remain authenticated without logging in again."
    )
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody TokenRequest request)
    {
        return ResponseEntity.ok(loginService.refreshToken(request));
    }
}
