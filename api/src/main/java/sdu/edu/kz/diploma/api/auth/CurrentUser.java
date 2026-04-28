package sdu.edu.kz.diploma.api.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import sdu.edu.kz.diploma.library.model.entity.User;

public final class CurrentUser {

    private CurrentUser() {
    }

    public static User get() {
        final var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User)) {
            throw new sdu.edu.kz.diploma.api.exception.BadRequestException("Not authenticated");
        }
        return (User) auth.getPrincipal();
    }

    public static Long studentId() {
        final var user = get();
        if (user.getStudent() == null) {
            throw new sdu.edu.kz.diploma.api.exception.BadRequestException("User is not linked to a student profile");
        }
        return user.getStudent().getId();
    }

    public static Long userId() {
        return get().getId();
    }
}