package sdu.edu.kz.diploma.api.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sdu.edu.kz.diploma.library.model.repository.UserRepository;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final SessionService sessionService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final var authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final var token = authHeader.substring(7);

        try {
            final var email = jwtService.extractEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (!sessionService.isSessionValid(token)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                final var user = userRepository.findByEmail(email).orElse(null);

                if (user != null && jwtService.isTokenValid(token, email) && user.isEmailVerified()) {
                    final var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
                    final var authToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception ignored) {
        }

        filterChain.doFilter(request, response);
    }
}