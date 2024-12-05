package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.Role;
import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.repository.RoleRepository;
import aminurdev.com.backend.domain.repository.UserRepository;
import aminurdev.com.backend.domain.request.UserRequest;
import aminurdev.com.backend.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(UserRequest request) {

        try {

            User user = new User();

            user.setName_en(request.getName_en());
            user.setName_bn(request.getName_bn());
            user.setEmail(request.getEmail());
            user.setPhone_en(request.getPhone_en());
            user.setPhone_bn(request.getPhone_bn());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            if (request.getRole() != null) {
                // Instead of fetching the role based on the request, fetch the specific role with id 4
                Role role = roleRepository.findById(4) // Fetch role with id 4
                        .orElseThrow(() -> new GlobalException("Role with id 4 is not found"));

                user.setRoles(role);
            }

            return userRepository.save(user);

        }catch (Exception exception){

            throw new GlobalException("Error while register user: " + exception.getMessage(), exception);
        }
    }
}
