package sdu.edu.kz.diploma.api.weekly.organize;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizeWeeklyResponse {

    private Long studentId;
    private Integer weekNumber;
    private String weeklySummary;
    private List<DailyPlanResponse> dailyPlans;
    private List<AdditionalTaskResponse> additionalTasks;
    private WellBeingResponse wellBeing;
    private LocalDateTime generatedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DailyPlanResponse {
        private String day;
        private List<TimeBlockResponse> timeBlocks;
        private String dailyTip;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TimeBlockResponse {
        private String startTime;
        private String endTime;
        private String taskTitle;
        private String courseCode;
        private String courseName;
        private String activityType;
        private String effortLevel;
        private String focusTip;
        private Integer estimatedMinutes;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AdditionalTaskResponse {
        private String courseCode;
        private String courseName;
        private String expectedGrade;
        private String taskTitle;
        private String description;
        private String reason;
        private String activityType;
        private String effortLevel;
        private Integer estimatedMinutes;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WellBeingResponse {
        private List<String> activeRestSuggestions;
        private List<String> breakStrategies;
        private List<String> nutritionTips;
        private List<String> sleepRecommendations;
        private List<String> mindfulnessTips;
        private String overallAdvice;
    }
}