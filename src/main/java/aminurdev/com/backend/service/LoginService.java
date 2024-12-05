package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.request.LoginRequest;
import aminurdev.com.backend.domain.request.TokenRequest;
import aminurdev.com.backend.domain.request.UserRequest;
import aminurdev.com.backend.domain.response.AuthResponse;

public interface LoginService {

    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(TokenRequest request);
}
