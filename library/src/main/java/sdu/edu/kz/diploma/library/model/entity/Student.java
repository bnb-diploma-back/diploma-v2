package sdu.edu.kz.diploma.library.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String studentId;

    private String department;

    private String major;

    private Integer enrollmentYear;

    @Enumerated(EnumType.STRING)
    private Semester currentSemester;

    private LocalDate dateOfBirth;

    @Column(columnDefinition = "TEXT")
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StudentSyllabus> studentSyllabi = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StudentCareer> studentCareers = new ArrayList<>();

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

    public void addStudentSyllabus(StudentSyllabus studentSyllabus) {
        studentSyllabi.add(studentSyllabus);
        studentSyllabus.setStudent(this);
    }

    public void removeStudentSyllabus(StudentSyllabus studentSyllabus) {
        studentSyllabi.remove(studentSyllabus);
        studentSyllabus.setStudent(null);
    }

    public void addStudentCareer(StudentCareer studentCareer) {
        studentCareers.add(studentCareer);
        studentCareer.setStudent(this);
    }

    public void removeStudentCareer(StudentCareer studentCareer) {
        studentCareers.remove(studentCareer);
        studentCareer.setStudent(null);
    }
}