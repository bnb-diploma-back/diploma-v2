package sdu.edu.kz.diploma.api.weekly.get;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetWeeklyResponse {

    private Long studentId;
    private String studentFirstName;
    private String studentLastName;
    private Integer weekNumber;
    private List<CourseTasksResponse> courses;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CourseTasksResponse {
        private Long syllabusId;
        private String courseCode;
        private String courseTitle;
        private String department;
        private String instructor;
        private Integer credits;
        private String semester;
        private List<TaskResponse> tasks;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TaskResponse {
        private Long id;
        private String title;
        private String description;
        private String instructions;
        private String taskType;
        private String status;
        private LocalDate dueDate;
        private Integer maxScore;
        private Integer score;
        private String feedback;
        private String submissionUrl;
        private LocalDateTime submittedAt;
        private LocalDateTime createdAt;
    }
}