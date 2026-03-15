package sdu.edu.kz.diploma.api.student.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.library.test.BaseTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetStudentApiTest extends BaseTest {

    @Autowired
    private GetStudentApi getStudentApi;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void findAll_returnsEmptyList_whenNoStudents() {
        final var result = getStudentApi.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_returnsAllStudents() {
        creator.student();
        creator.student();

        final var result = getStudentApi.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findById_returnsStudent() {
        final var student = creator.student();

        final var result = getStudentApi.findById(student.getId());

        assertThat(result.getId()).isEqualTo(student.getId());
        assertThat(result.getFirstName()).isEqualTo(student.getFirstName());
        assertThat(result.getLastName()).isEqualTo(student.getLastName());
        assertThat(result.getEmail()).isEqualTo(student.getEmail());
    }

    @Test
    void findById_throwsException_whenNotFound() {
        assertThatThrownBy(() -> getStudentApi.findById(999999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void findByStudentId_returnsStudent() {
        final var student = creator.student();

        final var result = getStudentApi.findByStudentId(student.getStudentId());

        assertThat(result.getStudentId()).isEqualTo(student.getStudentId());
    }

    @Test
    void findByStudentId_throwsException_whenNotFound() {
        assertThatThrownBy(() -> getStudentApi.findByStudentId("NONEXISTENT"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void findById_returnsStudentSyllabi() {
        final var student = creator.student();
        final var syllabus = creator.syllabus();
        creator.studentSyllabus(student, syllabus, "A+");

        final var result = getStudentApi.findById(student.getId());

        assertThat(result.getStudentSyllabi()).hasSize(1);
        assertThat(result.getStudentSyllabi().get(0).getExpectedGrade()).isEqualTo("A+");
        assertThat(result.getStudentSyllabi().get(0).getSyllabusTitle()).isEqualTo(syllabus.getTitle());
    }

    @Test
    void findById_returnsStudentCareers() {
        final var student = creator.student();
        creator.studentCareer(student, "Data Scientist");

        final var result = getStudentApi.findById(student.getId());

        assertThat(result.getStudentCareers()).hasSize(1);
        assertThat(result.getStudentCareers().get(0).getProfession()).isEqualTo("Data Scientist");
    }
}