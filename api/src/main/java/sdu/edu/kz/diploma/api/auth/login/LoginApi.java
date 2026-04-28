package sdu.edu.kz.diploma.api.auth.login;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.api.auth.AuthResponse;
import sdu.edu.kz.diploma.api.auth.JwtService;
import sdu.edu.kz.diploma.api.auth.SessionService;
import sdu.edu.kz.diploma.library.model.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LoginApi {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SessionService sessionService;

    @Transactional
    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        final var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new sdu.edu.kz.diploma.api.exception.BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new sdu.edu.kz.diploma.api.exception.BadRequestException("Invalid email or password");
        }

        final var token = jwtService.generateToken(user);

        sessionService.createSession(
                user,
                token,
                httpRequest.getRemoteAddr(),
                httpRequest.getHeader("User-Agent")
        );

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .studentId(user.getStudent() != null ? user.getStudent().getId() : null)
                .build();
    }
}