package sdu.edu.kz.diploma.api.career.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.library.test.BaseTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetCareerApiTest extends BaseTest {

    @Autowired
    private GetCareerApi getCareerApi;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void findByStudentId_throwsException_whenStudentNotFound() {
        assertThatThrownBy(() -> getCareerApi.findByStudentId(999999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void findByStudentId_returnsEmptyCareerCards_whenNoCareers() {
        final var student = creator.student();

        final var result = getCareerApi.findByStudentId(student.getId());

        assertThat(result.getStudentId()).isEqualTo(student.getId());
        assertThat(result.getStudentName()).isEqualTo(student.getFirstName() + " " + student.getLastName());
        assertThat(result.getCareerCards()).isEmpty();
    }

    @Test
    void findByStudentId_returnsCareerCards() {
        final var student = creator.student();
        creator.studentCareer(student, "Backend Developer");
        creator.studentCareer(student, "Data Scientist");
        creator.studentCareer(student, "DevOps Engineer");

        final var result = getCareerApi.findByStudentId(student.getId());

        assertThat(result.getCareerCards()).hasSize(3);
        assertThat(result.getCareerCards().stream().map(GetCareerResponse.CareerCardResponse::getProfession))
                .containsExactlyInAnyOrder("Backend Developer", "Data Scientist", "DevOps Engineer");
    }

    @Test
    void findByStudentId_returnsCareerCardDetails() {
        final var student = creator.student();
        final var career = creator.studentCareer(student, "ML Engineer");

        final var result = getCareerApi.findByStudentId(student.getId());

        final var card = result.getCareerCards().getFirst();
        assertThat(card.getId()).isEqualTo(career.getId());
        assertThat(card.getProfession()).isEqualTo("ML Engineer");
        assertThat(card.getDescription()).isEqualTo(career.getDescription());
        assertThat(card.getRequiredSkills()).isEqualTo(career.getRequiredSkills());
    }

    @Test
    void findByStudentId_doesNotReturnCareersFromOtherStudents() {
        final var student1 = creator.student();
        final var student2 = creator.student();
        creator.studentCareer(student1, "Backend Developer");
        creator.studentCareer(student2, "Frontend Developer");

        final var result = getCareerApi.findByStudentId(student1.getId());

        assertThat(result.getCareerCards()).hasSize(1);
        assertThat(result.getCareerCards().getFirst().getProfession()).isEqualTo("Backend Developer");
    }
}