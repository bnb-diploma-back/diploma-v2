package sdu.edu.kz.diploma.api.career.generate;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerAiInput {

    private String studentName;
    private String department;
    private String major;
    private Integer enrollmentYear;
    private String currentSemester;
    private List<CourseInfo> courses;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CourseInfo {
        private String courseCode;
        private String courseTitle;
        private String department;
        private Integer credits;
        private String expectedGrade;
        private String description;
        private String learningOutcomes;
    }
}