package sdu.edu.kz.diploma.library.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_syllabi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSyllabus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "syllabus_id", nullable = false)
    private Syllabus syllabus;

    private String expectedGrade;

    @Column(columnDefinition = "TEXT")
    private String notes;
}