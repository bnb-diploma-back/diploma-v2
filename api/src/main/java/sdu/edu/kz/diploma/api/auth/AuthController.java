package sdu.edu.kz.diploma.api.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sdu.edu.kz.diploma.api.auth.login.LoginApi;
import sdu.edu.kz.diploma.api.auth.login.LoginRequest;
import sdu.edu.kz.diploma.api.auth.register.RegisterApi;
import sdu.edu.kz.diploma.api.auth.register.RegisterRequest;
import sdu.edu.kz.diploma.api.auth.verify.ResendCodeApi;
import sdu.edu.kz.diploma.api.auth.verify.VerifyEmailApi;
import sdu.edu.kz.diploma.api.auth.verify.VerifyEmailRequest;
import sdu.edu.kz.diploma.library.model.entity.User;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration, login, logout with JWT tokens persisted as sessions")
public class AuthController {

    private final RegisterApi registerApi;
    private final LoginApi loginApi;
    private final SessionService sessionService;
    private final VerifyEmailApi verifyEmailApi;
    private final ResendCodeApi resendCodeApi;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new student user",
            description = """
                    Creates a new user account with STUDENT role.
                    Optionally bind to an existing student profile by providing studentId.
                    Returns a JWT token and creates a session in the database.""",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered successfully"),
                    @ApiResponse(responseCode = "500", description = "Email already registered or student not found")
            }
    )
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request,
                                                   HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerApi.register(request, httpRequest));
    }

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Register a new admin user",
            description = "Creates a new user account with ADMIN role. Only existing admins can create new admins.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Admin registered"),
                    @ApiResponse(responseCode = "403", description = "Not authorized — admin role required")
            }
    )
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody RegisterRequest request,
                                                       HttpServletRequest httpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerApi.registerAdmin(request, httpRequest));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = """
                    Authenticates a user by email and password.
                    Creates a session in the database and returns a JWT token.
                    Include the token in subsequent requests as: `Authorization: Bearer <token>`""",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful"),
                    @ApiResponse(responseCode = "500", description = "Invalid email or password")
            }
    )
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletRequest httpRequest) {
        return ResponseEntity.ok(loginApi.login(request, httpRequest));
    }

    @PostMapping("/verify")
    @Operation(
            summary = "Verify email",
            description = "Verifies the user's email with the 6-digit code sent on registration.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email verified successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid or expired code")
            }
    )
    public ResponseEntity<Void> verify(@Valid @RequestBody VerifyEmailRequest request) {
        verifyEmailApi.verify(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-code")
    @Operation(
            summary = "Resend verification code",
            description = "Sends a new 6-digit verification code to the given email address.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Code sent"),
                    @ApiResponse(responseCode = "400", description = "Email already verified or not found")
            }
    )
    public ResponseEntity<Void> resendCode(@RequestParam String email) {
        resendCodeApi.resend(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout",
            description = "Revokes the current session token. The token will no longer be accepted for authentication.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logged out successfully")
            }
    )
    public ResponseEntity<Void> logout(HttpServletRequest httpRequest) {
        final var authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            sessionService.revokeSession(authHeader.substring(7));
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout/all")
    @Operation(
            summary = "Logout from all devices",
            description = "Revokes all active sessions for the current user. All tokens will be invalidated.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All sessions revoked")
            }
    )
    public ResponseEntity<Void> logoutAll(@AuthenticationPrincipal User user) {
        if (user != null) {
            sessionService.revokeAllUserSessions(user.getId());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get current user info",
            description = "Returns the authenticated user's profile, role, and linked studentId. Use this after login to populate the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Current user info"),
                    @ApiResponse(responseCode = "401", description = "Not authenticated")
            }
    )
    public ResponseEntity<AuthResponse> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .studentId(user.getStudent() != null ? user.getStudent().getId() : null)
                .build());
    }
}