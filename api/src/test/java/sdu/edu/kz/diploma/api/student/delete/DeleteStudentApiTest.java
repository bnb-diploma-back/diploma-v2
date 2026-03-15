package sdu.edu.kz.diploma.api.student.delete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.library.test.BaseTest;
import sdu.edu.kz.diploma.library.test.repository.StudentTestRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeleteStudentApiTest extends BaseTest {

    @Autowired
    private DeleteStudentApi deleteStudentApi;

    @Autowired
    private StudentTestRepository studentTestRepository;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void delete_removesStudent() {
        final var student = creator.student();

        deleteStudentApi.delete(student.getId());

        assertThat(studentTestRepository.findById(student.getId())).isEmpty();
    }

    @Test
    void delete_removesStudentWithSyllabiAndCareers() {
        final var student = creator.student();
        final var syllabus = creator.syllabus();
        creator.studentSyllabus(student, syllabus);
        creator.studentCareer(student);

        deleteStudentApi.delete(student.getId());

        assertThat(studentTestRepository.findById(student.getId())).isEmpty();
    }

    @Test
    void delete_throwsException_whenNotFound() {
        assertThatThrownBy(() -> deleteStudentApi.delete(999999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void delete_doesNotAffectOtherStudents() {
        final var student1 = creator.student();
        final var student2 = creator.student();

        deleteStudentApi.delete(student1.getId());

        assertThat(studentTestRepository.findById(student1.getId())).isEmpty();
        assertThat(studentTestRepository.findById(student2.getId())).isPresent();
    }
}