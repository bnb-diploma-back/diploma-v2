package sdu.edu.kz.diploma.api.auth.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    @Pattern(
            regexp = "^[^@]+@(stu\\.sdu\\.edu\\.kz|sdu\\.edu\\.kz)$",
            message = "Only @stu.sdu.edu.kz or @sdu.edu.kz emails are allowed"
    )
    private String email;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private Long studentId;
}