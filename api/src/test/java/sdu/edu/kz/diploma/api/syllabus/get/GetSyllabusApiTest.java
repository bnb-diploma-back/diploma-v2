package sdu.edu.kz.diploma.api.syllabus.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.library.test.BaseTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetSyllabusApiTest extends BaseTest {

    @Autowired
    private GetSyllabusApi getSyllabusApi;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void findAll_returnsEmptyList_whenNoSyllabi() {
        final var result = getSyllabusApi.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_returnsAllSyllabi() {
        creator.syllabus();
        creator.syllabus();

        final var result = getSyllabusApi.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findById_returnsSyllabus() {
        final var syllabus = creator.syllabus();

        final var result = getSyllabusApi.findById(syllabus.getId());

        assertThat(result.getId()).isEqualTo(syllabus.getId());
        assertThat(result.getCourseCode()).isEqualTo(syllabus.getCourseCode());
        assertThat(result.getTitle()).isEqualTo(syllabus.getTitle());
    }

    @Test
    void findById_throwsException_whenNotFound() {
        assertThatThrownBy(() -> getSyllabusApi.findById(999999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void findByCourseCode_returnsSyllabus() {
        final var syllabus = creator.syllabus();

        final var result = getSyllabusApi.findByCourseCode(syllabus.getCourseCode());

        assertThat(result.getCourseCode()).isEqualTo(syllabus.getCourseCode());
    }

    @Test
    void findByCourseCode_throwsException_whenNotFound() {
        assertThatThrownBy(() -> getSyllabusApi.findByCourseCode("NONEXISTENT"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void findById_returnsWeeklyPlans() {
        final var syllabus = creator.syllabusWithWeeklyPlans(3);

        final var result = getSyllabusApi.findById(syllabus.getId());

        assertThat(result.getWeeklyPlans()).hasSize(3);
        assertThat(result.getWeeklyPlans().get(0).getWeekNumber()).isEqualTo(1);
        assertThat(result.getWeeklyPlans().get(1).getWeekNumber()).isEqualTo(2);
        assertThat(result.getWeeklyPlans().get(2).getWeekNumber()).isEqualTo(3);
    }

    @Test
    void findById_returnsUpdatedTitle_afterEdit() {
        final var syllabus = creator.syllabus();
        editor.updateTitle(syllabus.getId(), "Updated Title");

        final var result = getSyllabusApi.findById(syllabus.getId());

        assertThat(result.getTitle()).isEqualTo("Updated Title");
    }
}