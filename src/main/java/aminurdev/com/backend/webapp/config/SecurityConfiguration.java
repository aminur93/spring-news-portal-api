package aminurdev.com.backend.webapp.config;

import aminurdev.com.backend.domain.response.AuthResponse;
import aminurdev.com.backend.service.LogoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationProvider authenticationProvider;

    private final LogoutHandler logoutHandler;

    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/api/v1/public/**",
                                "/images/**",
                                "/swagger-ui/**",       // Allow access to Swagger UI resources
                                "/v3/api-docs/**",      // Allow access to API docs (OpenAPI specification)
                                "/swagger-ui.html",     // Swagger UI main page
                                "/swagger-ui/index.html" // Swagger UI HTML page
                        ).permitAll()
                        .anyRequest().authenticated())

                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
//                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> {
                    SecurityContextHolder.clearContext();

                    AuthResponse authResponse = new AuthResponse();
                    authResponse.setMessage("Logged out successfully");
                    authResponse.setStatusCode(HttpStatus.OK.value());

                    // Serialize the AuthResponse object to JSON
                    ObjectMapper objectMapper = new ObjectMapper();
                    String responseBody = objectMapper.writeValueAsString(authResponse);

                    // Set response content type and write the response body
                    response.setContentType("application/json");
                    response.getWriter().write(responseBody);
                });

        return http.build();
    }

    @Bean("authenticationEntryPoint")
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                // Log the unauthorized access attempt
                System.out.println("Unauthorized access attempt: " + request.getRequestURI());

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("{ \"status\": \""+ HttpStatus.UNAUTHORIZED.value()+"\", \"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\" }");
            }
        };
    }
}
