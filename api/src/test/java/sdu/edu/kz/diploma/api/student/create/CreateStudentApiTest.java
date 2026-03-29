package sdu.edu.kz.diploma.api.student.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.library.model.enums.Semester;
import sdu.edu.kz.diploma.library.test.BaseTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CreateStudentApiTest extends BaseTest {

    @Autowired
    private CreateStudentApi createStudentApi;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void create_returnsStudentId() {
        final var department = creator.department();
        final var major = creator.major(department);
        final var request = CreateStudentRequest.builder()
                .firstName(randomizer.name())
                .lastName(randomizer.name())
                .email(randomizer.email())
                .studentId(randomizer.code())
                .departmentId(department.getId())
                .majorId(major.getId())
                .enrollmentYear(2025)
                .currentSemester(Semester.FALL)
                .dateOfBirth(LocalDate.of(2002, 5, 15))
                .build();

        final var id = createStudentApi.create(request);

        assertThat(id).isNotNull();
    }

    @Test
    void create_persistsStudentWithSyllabiAndCareers() {
        final var syllabus = creator.syllabus();

        final var request = CreateStudentRequest.builder()
                .firstName(randomizer.name())
                .lastName(randomizer.name())
                .email(randomizer.email())
                .studentId(randomizer.code())
                .studentSyllabi(List.of(
                        CreateStudentRequest.StudentSyllabusRequest.builder()
                                .syllabusId(syllabus.getId())
                                .expectedGrade("A")
                                .build()
                ))
                .studentCareers(List.of(
                        CreateStudentRequest.StudentCareerRequest.builder()
                                .profession("Software Engineer")
                                .description(randomizer.text())
                                .build()
                ))
                .build();

        final var id = createStudentApi.create(request);

        assertThat(id).isNotNull();
    }

    @Test
    void create_setsCorrectFields() {
        final var firstName = randomizer.name();
        final var lastName = randomizer.name();
        final var request = CreateStudentRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(randomizer.email())
                .studentId(randomizer.code())
                .enrollmentYear(2024)
                .currentSemester(Semester.SPRING)
                .build();

        final var id = createStudentApi.create(request);

        assertThat(id).isNotNull();
        final var found = creator.student();
        assertThat(found).isNotNull();
    }
}