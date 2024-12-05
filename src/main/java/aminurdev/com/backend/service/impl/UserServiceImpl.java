package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.Role;
import aminurdev.com.backend.domain.entity.User;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.exception.ResourceNotFoundException;
import aminurdev.com.backend.domain.repository.RoleRepository;
import aminurdev.com.backend.domain.repository.UserRepository;
import aminurdev.com.backend.domain.request.UserRequest;
import aminurdev.com.backend.domain.response.pagination.Links;
import aminurdev.com.backend.domain.response.pagination.Meta;
import aminurdev.com.backend.domain.response.pagination.PaginationResponse;
import aminurdev.com.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public PaginationResponse<User> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction,"updatedAt"));

        Page<User> userPage = userRepository.findAll(pageable);

        List<User> users = userPage.getContent();

        PaginationResponse<User> response = new PaginationResponse<>();

        response.setData(users);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All user fetch successful");

        Meta meta = new Meta();

        meta.setCurrentPage(userPage.getNumber() + 1);
        meta.setFrom(userPage.getNumber() * userPage.getSize() + 1);
        meta.setLastPage(userPage.getTotalPages());
        meta.setPath("/user");
        meta.setPerPage(userPage.getSize());
        meta.setTo((int) userPage.getTotalElements());
        meta.setTotal((int) userPage.getTotalElements());
        response.setMeta(meta);

        Links links = new Links();

        links.setFirst("/user?page=1");
        links.setLast("/user?page=" + userPage.getTotalPages());
        if (userPage.hasPrevious()) {
            links.setPrev("/user?page=" + userPage.previousPageable().getPageNumber());
        }
        if (userPage.hasNext()) {
            links.setNext("/user?page=" + userPage.nextPageable().getPageNumber());
        }

        response.setLinks(links);

        return response;
    }

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public User store(UserRequest request) {

        try{

            User user = User.builder()
                    .name_en(request.getName_en())
                    .name_bn(request.getName_bn())
                    .phone_en(request.getPhone_en())
                    .phone_bn(request.getPhone_bn())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            User savedUser = userRepository.save(user);

            if (request.getRole() != null)
            {
                Role role = roleRepository.findById(request.getRole())
                        .orElseThrow(() -> new GlobalException("Role is not found " + request.getRole()));

                savedUser.setRoles(role);
            }

            return userRepository.save(savedUser);

        }catch (Exception exception){

            throw new GlobalException("Error While User Storing : " + exception.getMessage(), exception);
        }
    }

    @Override
    public User edit(Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException("User is not found : "+userId));

        return user;
    }

    @Override
    public User update(UserRequest request, Integer userId) {
        try{
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

            user.setName_en(request.getName_en());
            user.setName_bn(request.getName_bn());
            user.setPhone_en(request.getPhone_en());
            user.setPhone_bn(request.getPhone_bn());
            user.setEmail(request.getEmail());

            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                // New password provided, encode and set it
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            } else {
                // No new password provided, keep the old password
                user.setPassword(user.getPassword()); // This is essentially a no-op
            }

            // Save the User to the database to obtain its ID
            User savedUser = userRepository.save(user);

            if (request.getRole() != null) {
                Role role = roleRepository.findById(request.getRole())
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

                // Set the fetched roles to the saved User
                savedUser.setRoles(role);
            }

            // Save the updated User with associations to the database
            return userRepository.save(savedUser);

        }catch (Exception exception){

            throw new GlobalException("Error while updating user: " + exception.getMessage(), exception);
        }
    }

    @Override
    public void destroy(Integer userId) {

        try{
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

            if (user.getId() != null) {
                // Delete all roles associated with the user
                userRepository.deleteUserRoles(userId);
            }

            userRepository.deleteById(userId);
        }catch (Exception exception){

            throw new GlobalException("Error while deleting user: " + exception.getMessage(), exception);
        }

    }
}
