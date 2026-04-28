package sdu.edu.kz.diploma.api.auth;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final Resend resend;

    @Value("${resend.from:onboarding@resend.dev}")
    private String from;

    public EmailService(@Value("${resend.api-key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    public void sendVerificationCode(String toEmail, String firstName, String code) {
        final var params = CreateEmailOptions.builder()
                .from(from)
                .to(toEmail)
                .subject("Your verification code")
                .html(buildEmailHtml(firstName, code))
                .build();

        try {
            resend.emails().send(params);
            log.info("Verification email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", toEmail, e.getMessage());
            throw new sdu.edu.kz.diploma.api.exception.ServiceException("Failed to send verification email", e);
        }
    }

    private String buildEmailHtml(String firstName, String code) {
        return """
                <div style="font-family: Arial, sans-serif; max-width: 480px; margin: 0 auto; padding: 32px;">
                    <h2 style="color: #1a1a1a;">Verify your email</h2>
                    <p>Hi %s,</p>
                    <p>Use the code below to verify your email address. It expires in 15 minutes.</p>
                    <div style="background: #f4f4f4; border-radius: 8px; padding: 24px; text-align: center; margin: 24px 0;">
                        <span style="font-size: 36px; font-weight: bold; letter-spacing: 8px; color: #1a1a1a;">%s</span>
                    </div>
                    <p style="color: #666; font-size: 14px;">If you didn't request this, you can safely ignore this email.</p>
                </div>
                """.formatted(firstName, code);
    }
}