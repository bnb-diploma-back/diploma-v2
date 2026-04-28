package sdu.edu.kz.diploma.api.auth.register;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.api.auth.AuthResponse;
import sdu.edu.kz.diploma.api.auth.EmailService;
import sdu.edu.kz.diploma.api.auth.JwtService;
import sdu.edu.kz.diploma.api.auth.SessionService;
import sdu.edu.kz.diploma.api.auth.verify.ResendCodeApi;
import sdu.edu.kz.diploma.library.model.enums.Role;
import sdu.edu.kz.diploma.library.model.entity.User;
import sdu.edu.kz.diploma.library.model.repository.StudentRepository;
import sdu.edu.kz.diploma.library.model.repository.UserRepository;

import sdu.edu.kz.diploma.api.exception.BadRequestException;
import sdu.edu.kz.diploma.api.exception.ConflictException;
import sdu.edu.kz.diploma.api.exception.NotFoundException;
import sdu.edu.kz.diploma.api.exception.ServiceException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegisterApi {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SessionService sessionService;
    private final EmailService emailService;

    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest) {
        if (!request.getEmail().endsWith("@sdu.edu.kz")) {
            throw new BadRequestException("Only @sdu.edu.kz emails are allowed");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already registered: " + request.getEmail());
        }

        final var code = ResendCodeApi.generateCode();

        final var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .emailVerified(false)
                .verificationCode(code)
                .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15))
                .build();

        if (request.getStudentId() != null) {
            final var student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new NotFoundException("Student not found with id: " + request.getStudentId()));
            user.setStudent(student);
        }

        final var saved = userRepository.save(user);
        final var token = jwtService.generateToken(saved);

        sessionService.createSession(
                saved,
                token,
                httpRequest.getRemoteAddr(),
                httpRequest.getHeader("User-Agent")
        );

        emailService.sendVerificationCode(saved.getEmail(), saved.getFirstName(), code);

        return toResponse(saved, token);
    }

    @Transactional
    public AuthResponse registerAdmin(RegisterRequest request, HttpServletRequest httpRequest) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already registered: " + request.getEmail());
        }

        final var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();

        final var saved = userRepository.save(user);
        final var token = jwtService.generateToken(saved);

        sessionService.createSession(
                saved,
                token,
                httpRequest.getRemoteAddr(),
                httpRequest.getHeader("User-Agent")
        );

        return toResponse(saved, token);
    }

    private AuthResponse toResponse(User user, String token) {
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