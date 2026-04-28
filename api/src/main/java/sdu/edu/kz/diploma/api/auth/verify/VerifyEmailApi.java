package sdu.edu.kz.diploma.api.auth.verify;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerifyEmailApi {

    private final UserRepository userRepository;

    @Transactional
    public void verify(VerifyEmailRequest request) {
        final var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new sdu.edu.kz.diploma.api.exception.NotFoundException("User not found"));

        if (user.isEmailVerified()) {
            return;
        }

        if (user.getVerificationCode() == null
                || !user.getVerificationCode().equals(request.getCode())) {
            throw new sdu.edu.kz.diploma.api.exception.BadRequestException("Invalid verification code");
        }

        if (user.getVerificationCodeExpiresAt() == null
                || user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new sdu.edu.kz.diploma.api.exception.BadRequestException("Verification code has expired");
        }

        user.setEmailVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        userRepository.save(user);
    }
}