package sdu.edu.kz.diploma.library.model.syllabus.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "weekly_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer weekNumber;

    @Column(nullable = false)
    private String topic;

    @Column(columnDefinition = "TEXT")
    private String learningObjectives;

    @Column(columnDefinition = "TEXT")
    private String lectureContent;

    @Column(columnDefinition = "TEXT")
    private String practiceContent;

    @Column(columnDefinition = "TEXT")
    private String assignments;

    @Column(columnDefinition = "TEXT")
    private String readings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "syllabus_id", nullable = false)
    private Syllabus syllabus;
}