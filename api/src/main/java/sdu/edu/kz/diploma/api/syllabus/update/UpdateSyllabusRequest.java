package sdu.edu.kz.diploma.api.syllabus.update;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import sdu.edu.kz.diploma.library.model.entity.Semester;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSyllabusRequest {

    @NotBlank
    private String courseCode;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Integer credits;

    private String department;
    private String instructor;
    private String prerequisites;
    private String objectives;
    private String learningOutcomes;
    private String assessmentCriteria;
    private String requiredTextbooks;
    private String recommendedReading;
    private String academicYear;
    private Semester semester;
    private LocalDate startDate;
    private LocalDate endDate;

    @Valid
    private List<WeeklyPlanRequest> weeklyPlans;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WeeklyPlanRequest {

        @NotNull
        @Min(1)
        @Max(15)
        private Integer weekNumber;

        @NotBlank
        private String topic;

        private String learningObjectives;
        private String lectureContent;
        private String practiceContent;
        private String assignments;
        private String readings;
    }
}