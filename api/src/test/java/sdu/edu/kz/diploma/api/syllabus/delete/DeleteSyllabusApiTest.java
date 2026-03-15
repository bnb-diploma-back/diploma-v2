package sdu.edu.kz.diploma.api.syllabus.delete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.library.test.BaseTest;
import sdu.edu.kz.diploma.library.test.repository.SyllabusTestRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeleteSyllabusApiTest extends BaseTest {

    @Autowired
    private DeleteSyllabusApi deleteSyllabusApi;

    @Autowired
    private SyllabusTestRepository syllabusTestRepository;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void delete_removesSyllabus() {
        final var syllabus = creator.syllabus();

        deleteSyllabusApi.delete(syllabus.getId());

        assertThat(syllabusTestRepository.findById(syllabus.getId())).isEmpty();
    }

    @Test
    void delete_removesSyllabusWithWeeklyPlans() {
        final var syllabus = creator.syllabusWithWeeklyPlans(5);

        deleteSyllabusApi.delete(syllabus.getId());

        assertThat(syllabusTestRepository.findById(syllabus.getId())).isEmpty();
    }

    @Test
    void delete_throwsException_whenNotFound() {
        assertThatThrownBy(() -> deleteSyllabusApi.delete(999999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void delete_doesNotAffectOtherSyllabi() {
        final var syllabus1 = creator.syllabus();
        final var syllabus2 = creator.syllabus();

        deleteSyllabusApi.delete(syllabus1.getId());

        assertThat(syllabusTestRepository.findById(syllabus1.getId())).isEmpty();
        assertThat(syllabusTestRepository.findById(syllabus2.getId())).isPresent();
    }
}