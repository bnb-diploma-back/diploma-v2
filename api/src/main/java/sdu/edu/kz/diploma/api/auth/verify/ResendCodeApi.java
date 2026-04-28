package sdu.edu.kz.diploma.api.auth.verify;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.api.auth.EmailService;
import sdu.edu.kz.diploma.library.model.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ResendCodeApi {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    public void resend(String email) {
        final var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new sdu.edu.kz.diploma.api.exception.NotFoundException("User not found"));

        if (user.isEmailVerified()) {
            throw new sdu.edu.kz.diploma.api.exception.BadRequestException("Email already verified");
        }

        final var code = generateCode();
        user.setVerificationCode(code);
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        emailService.sendVerificationCode(user.getEmail(), user.getFirstName(), code);
    }

    public static String generateCode() {
        return String.format("%06d", new Random().nextInt(1_000_000));
    }
}