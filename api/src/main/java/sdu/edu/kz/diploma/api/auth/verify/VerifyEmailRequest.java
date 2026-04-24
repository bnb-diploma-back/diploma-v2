package sdu.edu.kz.diploma.api.auth.verify;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerifyEmailRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String code;
}