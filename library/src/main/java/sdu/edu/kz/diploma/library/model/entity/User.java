package sdu.edu.kz.diploma.library.model.entity;

import jakarta.persistence.*;
import lombok.*;
import sdu.edu.kz.diploma.library.model.enums.Role;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.STUDENT;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", unique = true)
    private Student student;

    @Builder.Default
    private boolean emailVerified = false;

    private String verificationCode;

    private LocalDateTime verificationCodeExpiresAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}