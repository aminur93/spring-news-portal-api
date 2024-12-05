package aminurdev.com.backend.service;

import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.request.UserRequest;

public interface RegisterService {

    User register(UserRequest request);
}
