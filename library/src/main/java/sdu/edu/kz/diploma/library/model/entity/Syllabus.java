package sdu.edu.kz.diploma.library.model.entity;

import jakarta.persistence.*;
import lombok.*;
import sdu.edu.kz.diploma.library.model.enums.Semester;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "syllabi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Syllabus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String courseCode;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer credits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    private String instructor;

    @Column(columnDefinition = "TEXT")
    private String prerequisites;

    @Column(columnDefinition = "TEXT")
    private String objectives;

    @Column(columnDefinition = "TEXT")
    private String learningOutcomes;

    @Column(columnDefinition = "TEXT")
    private String assessmentCriteria;

    @Column(columnDefinition = "TEXT")
    private String requiredTextbooks;

    @Column(columnDefinition = "TEXT")
    private String recommendedReading;

    private String academicYear;

    @Enumerated(EnumType.STRING)
    private Semester semester;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "syllabus", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("weekNumber ASC")
    @Builder.Default
    private List<WeeklyPlan> weeklyPlans = new ArrayList<>();

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

    public void addWeeklyPlan(WeeklyPlan plan) {
        weeklyPlans.add(plan);
        plan.setSyllabus(this);
    }

    public void removeWeeklyPlan(WeeklyPlan plan) {
        weeklyPlans.remove(plan);
        plan.setSyllabus(null);
    }
}