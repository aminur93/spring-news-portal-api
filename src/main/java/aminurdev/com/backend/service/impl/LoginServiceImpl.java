package aminurdev.com.backend.service.impl;

import aminurdev.com.backend.domain.entity.*;
import aminurdev.com.backend.domain.exception.GlobalException;
import aminurdev.com.backend.domain.exception.ResourceNotFoundException;
import aminurdev.com.backend.domain.repository.TokenRepository;
import aminurdev.com.backend.domain.repository.UserRepository;
import aminurdev.com.backend.domain.request.LoginRequest;
import aminurdev.com.backend.domain.request.TokenRequest;
import aminurdev.com.backend.domain.request.UserRequest;
import aminurdev.com.backend.domain.response.AuthResponse;
import aminurdev.com.backend.service.JwtService;
import aminurdev.com.backend.service.LoginService;
import aminurdev.com.backend.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;

    private final MenuService menuService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    private  static  final long EXPIRATION_TIME = 86400000;

    @Override
    public AuthResponse login(LoginRequest request) {

        AuthResponse response = new AuthResponse();

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

            // Collect user permissions from their roles
            List<Permission> permissions = new ArrayList<>();

            for (Role role :  Collections.singletonList(user.getRoles())) {
                permissions.addAll(role.getPermissions());
            }

            // Create a list of permission IDs
            List<Integer> permissionIds = permissions.stream()
                    .map(Permission::getId) // Assuming getId() returns Integer
                    .toList();

            String jwtToken = jwtService.generateToken(user);

            revokedAllUserTokens(user);

            saveUserToken(user, jwtToken);

            String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

            if (user.getId() > 0)
            {
                response.setUser(user);

                Map<String, Object> roleUpdate = new HashMap<>();

                roleUpdate.put("id", user.getRoles().getId());
                roleUpdate.put("name_en", user.getRoles().getName_en());
                roleUpdate.put("name_bn", user.getRoles().getName_bn());
                roleUpdate.put("created_at", user.getRoles().getCreatedAt());
                roleUpdate.put("updated_at", user.getRoles().getUpdatedAt());

                response.setRole(roleUpdate);

                response.setPermissions(permissions);

                // Fetch all menus from the menu service
                List<Menu> allMenus = menuService.getAllMenus();

                // Filter menus based on user permissions
                List<Menu> filteredMenus = allMenus.stream()
                        .filter(menu -> menu.getPermission() == null ||
                                permissionIds.contains(menu.getPermission().getId())) // Ensure permissionId is Integer
                        .collect(Collectors.toList());

                // Optionally, build the menu tree (if needed)
                List<Menu> menuTree = buildMenuTree(filteredMenus, null);

                // Set the filtered and hierarchical menus in the response
                response.setMenus(menuTree);


                response.setMessage("Login successful");
                response.setStatusCode(HttpStatus.OK.value());
                response.setToken(jwtToken);
                response.setRefreshToken(refreshToken);
                response.setExpiration(EXPIRATION_TIME);
            }

        }catch (Exception exception){

            throw new GlobalException("Error while login user : " + exception.getMessage(), exception);
        }

        return response;
    }

    private List<Menu> buildMenuTree(List<Menu> menus, Integer parentId) {
        List<Menu> menuTree = new ArrayList<>();

        for (Menu menu : menus) {
            if (menu.getParent_id() == null ? parentId == null : menu.getParent_id().equals(parentId)) {
                // Recursively build the children menu tree
                menu.setChildren(buildMenuTree(menus, menu.getId()));
                menuTree.add(menu);
            }
        }

        return menuTree;
    }

    @Override
    public AuthResponse refreshToken(TokenRequest request) {

        AuthResponse response = new AuthResponse();

        try{

            String userEmail = jwtService.extractUserName(request.getRefreshToken());

            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

            if (jwtService.isValidToken(request.getRefreshToken(), user)) {

                var jwt = jwtService.generateToken(user);

                revokedAllUserTokens(user);

                saveUserToken(user, jwt);

                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(request.getRefreshToken());
                response.setExpiration(EXPIRATION_TIME);
                response.setMessage("Successfully Refreshed Token");
            }

        }catch (Exception e){

            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

            response.setMessage(e.getMessage());
        }

        return  response;
    }

    private void revokedAllUserTokens(User user)
    {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        if ((validUserTokens.isEmpty()))
        {
            return;
        }

        validUserTokens.forEach(t -> {
            t.setExpired(1);
            t.setRevoked(1);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    public void saveUserToken(User user, String jwtToken)
    {
        Token token = new Token();

        token.setUser(user);
        token.setToken(jwtToken);
        token.setTokenType(TokenType.BEARER);
        token.setExpired(0);
        token.setRevoked(0);

        tokenRepository.save(token);
    }
}
