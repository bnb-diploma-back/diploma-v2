package sdu.edu.kz.diploma.api.student.update;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.api.student.get.GetStudentApi;
import sdu.edu.kz.diploma.library.test.BaseTest;
import sdu.edu.kz.diploma.library.test.repository.StudentTestRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UpdateStudentApiTest extends BaseTest {

    @Autowired
    private UpdateStudentApi updateStudentApi;

    @Autowired
    private GetStudentApi getStudentApi;

    @Autowired
    private StudentTestRepository studentTestRepository;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void update_changesFirstName() {
        final var student = creator.student();
        final var newFirstName = randomizer.name();

        final var request = UpdateStudentRequest.builder()
                .firstName(newFirstName)
                .lastName(student.getLastName())
                .email(student.getEmail())
                .studentId(student.getStudentId())
                .build();

        updateStudentApi.update(student.getId(), request);

        final var updated = studentTestRepository.findById(student.getId()).orElseThrow();
        assertThat(updated.getFirstName()).isEqualTo(newFirstName);
    }

    @Test
    void update_replacesStudentSyllabi() {
        final var student = creator.student();
        final var syllabus1 = creator.syllabus();
        final var syllabus2 = creator.syllabus();
        creator.studentSyllabus(student, syllabus1);

        final var request = UpdateStudentRequest.builder()
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .studentId(student.getStudentId())
                .studentSyllabi(List.of(
                        UpdateStudentRequest.StudentSyllabusRequest.builder()
                                .syllabusId(syllabus2.getId())
                                .expectedGrade("B+")
                                .build()
                ))
                .build();

        updateStudentApi.update(student.getId(), request);

        final var result = getStudentApi.findById(student.getId());
        assertThat(result.getStudentSyllabi()).hasSize(1);
        assertThat(result.getStudentSyllabi().get(0).getExpectedGrade()).isEqualTo("B+");
    }

    @Test
    void update_replacesStudentCareers() {
        final var student = creator.student();
        creator.studentCareer(student, "Old Profession");

        final var request = UpdateStudentRequest.builder()
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .studentId(student.getStudentId())
                .studentCareers(List.of(
                        UpdateStudentRequest.StudentCareerRequest.builder()
                                .profession("New Profession")
                                .build()
                ))
                .build();

        updateStudentApi.update(student.getId(), request);

        final var result = getStudentApi.findById(student.getId());
        assertThat(result.getStudentCareers()).hasSize(1);
        assertThat(result.getStudentCareers().get(0).getProfession()).isEqualTo("New Profession");
    }

    @Test
    void update_throwsException_whenNotFound() {
        final var request = UpdateStudentRequest.builder()
                .firstName(randomizer.name())
                .lastName(randomizer.name())
                .email(randomizer.email())
                .studentId(randomizer.code())
                .build();

        assertThatThrownBy(() -> updateStudentApi.update(999999L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }
}