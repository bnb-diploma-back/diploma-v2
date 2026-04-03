package sdu.edu.kz.diploma.api.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> {})
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()

                        // Dictionary — read for all authenticated, write for admins
                        .requestMatchers(HttpMethod.GET, "/api/v1/dictionary/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/dictionary/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/dictionary/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/dictionary/**").hasRole("ADMIN")

                        // Syllabi — read for all authenticated, write for admins
                        .requestMatchers(HttpMethod.GET, "/api/v1/syllabi/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/syllabi/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/syllabi/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/syllabi/**").hasRole("ADMIN")

                        // Parser — admin only
                        .requestMatchers("/api/v1/parser/**").hasRole("ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}