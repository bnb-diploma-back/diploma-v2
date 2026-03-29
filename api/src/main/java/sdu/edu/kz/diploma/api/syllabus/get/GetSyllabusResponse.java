package sdu.edu.kz.diploma.api.syllabus.get;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetSyllabusResponse {

    private Long id;
    private String courseCode;
    private String title;
    private String description;
    private Integer credits;
    private Long departmentId;
    private String departmentName;
    private Long majorId;
    private String majorName;
    private String instructor;
    private String prerequisites;
    private String objectives;
    private String learningOutcomes;
    private String assessmentCriteria;
    private String requiredTextbooks;
    private String recommendedReading;
    private String academicYear;
    private String semester;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<WeeklyPlanResponse> weeklyPlans;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WeeklyPlanResponse {
        private Long id;
        private Integer weekNumber;
        private String topic;
        private String learningObjectives;
        private String lectureContent;
        private String practiceContent;
        private String assignments;
        private String readings;
    }
}