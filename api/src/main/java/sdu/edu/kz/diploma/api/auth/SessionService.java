package sdu.edu.kz.diploma.api.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.entity.Session;
import sdu.edu.kz.diploma.library.model.entity.User;
import sdu.edu.kz.diploma.library.model.repository.SessionRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    @Value("${jwt.expiration:86400000}")
    private long expirationMs;

    @Transactional
    public Session createSession(User user, String token, String ipAddress, String userAgent) {
        final var session = Session.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(expirationMs / 1000))
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();

        return sessionRepository.save(session);
    }

    @Transactional(readOnly = true)
    public boolean isSessionValid(String token) {
        final var session = sessionRepository.findByToken(token).orElse(null);
        if (session == null) {
            return false;
        }
        return !session.isRevoked() && session.getExpiresAt().isAfter(LocalDateTime.now());
    }

    @Transactional
    public void revokeSession(String token) {
        final var session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        session.setRevoked(true);
        sessionRepository.save(session);
    }

    @Transactional
    public void revokeAllUserSessions(Long userId) {
        final var sessions = sessionRepository.findByUserIdAndRevokedFalse(userId);
        sessions.forEach(session -> session.setRevoked(true));
        sessionRepository.saveAll(sessions);
    }
}