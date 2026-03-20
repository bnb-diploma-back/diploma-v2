package sdu.edu.kz.diploma.api.dashboard.get;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetDashboardResponse {

    private StudentProfileResponse profile;
    private AcademicOverviewResponse academicOverview;
    private CurrentWeekResponse currentWeek;
    private List<UpcomingDeadlineResponse> upcomingDeadlines;
    private TaskProgressResponse taskProgress;
    private List<TopCareerResponse> topCareers;
    private TodayScheduleResponse todaySchedule;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentProfileResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String studentId;
        private String department;
        private String major;
        private Integer enrollmentYear;
        private String currentSemester;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AcademicOverviewResponse {
        private Integer totalCourses;
        private Integer totalCredits;
        private List<CourseSnapshotResponse> courses;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CourseSnapshotResponse {
        private Long syllabusId;
        private String courseCode;
        private String courseTitle;
        private String instructor;
        private Integer credits;
        private String expectedGrade;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CurrentWeekResponse {
        private Integer weekNumber;
        private Integer totalTasks;
        private Integer completedTasks;
        private Integer pendingTasks;
        private Integer overdueTasks;
        private boolean hasOrganizer;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpcomingDeadlineResponse {
        private Long taskId;
        private String taskTitle;
        private String courseCode;
        private String courseTitle;
        private String taskType;
        private String status;
        private LocalDate dueDate;
        private Integer daysRemaining;
        private Integer maxScore;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TaskProgressResponse {
        private Integer totalTasks;
        private Integer pendingCount;
        private Integer inProgressCount;
        private Integer submittedCount;
        private Integer gradedCount;
        private Integer overdueCount;
        private Integer completionPercentage;
        private Double averageScore;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopCareerResponse {
        private String profession;
        private String industryDomain;
        private String demandLevel;
        private Integer matchPercentage;
        private String whyThisFits;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TodayScheduleResponse {
        private String day;
        private String dailyTip;
        private List<TimeBlockResponse> timeBlocks;
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
        private String activityType;
        private String effortLevel;
        private String focusTip;
        private Integer estimatedMinutes;
    }
}