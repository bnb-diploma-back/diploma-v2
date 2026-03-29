package sdu.edu.kz.diploma.api.student.update;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import sdu.edu.kz.diploma.library.model.enums.Semester;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStudentRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @NotBlank
    private String studentId;

    private Long departmentId;
    private Long majorId;
    private Integer enrollmentYear;
    private Semester currentSemester;
    private LocalDate dateOfBirth;
    private String phone;
    private String address;

    @Valid
    private List<StudentSyllabusRequest> studentSyllabi;

    @Valid
    private List<StudentCareerRequest> studentCareers;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentSyllabusRequest {

        @NotNull
        private Long syllabusId;

        private String expectedGrade;
        private String notes;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentCareerRequest {

        @NotBlank
        private String profession;

        private String description;
        private String requiredSkills;
    }
}