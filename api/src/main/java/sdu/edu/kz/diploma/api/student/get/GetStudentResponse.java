package sdu.edu.kz.diploma.api.student.get;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetStudentResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String studentId;
    private String department;
    private String major;
    private Integer enrollmentYear;
    private String currentSemester;
    private LocalDate dateOfBirth;
    private String phone;
    private String address;
    private List<StudentSyllabusResponse> studentSyllabi;
    private List<StudentCareerResponse> studentCareers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentSyllabusResponse {
        private Long id;
        private Long syllabusId;
        private String syllabusTitle;
        private String syllabusCourseCode;
        private String expectedGrade;
        private String notes;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentCareerResponse {
        private Long id;
        private String profession;
        private String description;
        private String requiredSkills;
    }
}