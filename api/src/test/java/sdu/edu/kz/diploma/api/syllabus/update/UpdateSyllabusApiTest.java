package sdu.edu.kz.diploma.api.syllabus.update;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.api.syllabus.get.GetSyllabusApi;
import sdu.edu.kz.diploma.library.model.entity.Semester;
import sdu.edu.kz.diploma.library.test.BaseTest;
import sdu.edu.kz.diploma.library.test.repository.SyllabusTestRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UpdateSyllabusApiTest extends BaseTest {

    @Autowired
    private UpdateSyllabusApi updateSyllabusApi;

    @Autowired
    private GetSyllabusApi getSyllabusApi;

    @Autowired
    private SyllabusTestRepository syllabusTestRepository;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void update_changesTitle() {
        final var syllabus = creator.syllabus();
        final var newTitle = randomizer.name();

        final var request = UpdateSyllabusRequest.builder()
                .courseCode(syllabus.getCourseCode())
                .title(newTitle)
                .credits(syllabus.getCredits())
                .build();

        updateSyllabusApi.update(syllabus.getId(), request);

        final var updated = syllabusTestRepository.findById(syllabus.getId()).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo(newTitle);
    }

    @Test
    void update_changesSemester() {
        final var syllabus = creator.syllabus();

        final var request = UpdateSyllabusRequest.builder()
                .courseCode(syllabus.getCourseCode())
                .title(syllabus.getTitle())
                .credits(syllabus.getCredits())
                .semester(Semester.SUMMER)
                .build();

        updateSyllabusApi.update(syllabus.getId(), request);

        final var updated = syllabusTestRepository.findById(syllabus.getId()).orElseThrow();
        assertThat(updated.getSemester()).isEqualTo(Semester.SUMMER);
    }

    @Test
    void update_replacesWeeklyPlans() {
        final var syllabus = creator.syllabusWithWeeklyPlans(3);

        final var request = UpdateSyllabusRequest.builder()
                .courseCode(syllabus.getCourseCode())
                .title(syllabus.getTitle())
                .credits(syllabus.getCredits())
                .weeklyPlans(List.of(
                        UpdateSyllabusRequest.WeeklyPlanRequest.builder()
                                .weekNumber(1)
                                .topic("New Week 1")
                                .build()
                ))
                .build();

        updateSyllabusApi.update(syllabus.getId(), request);

        final var updated = getSyllabusApi.findById(syllabus.getId());
        assertThat(updated.getWeeklyPlans()).hasSize(1);
        assertThat(updated.getWeeklyPlans().get(0).getTopic()).isEqualTo("New Week 1");
    }

    @Test
    void update_throwsException_whenNotFound() {
        final var request = UpdateSyllabusRequest.builder()
                .courseCode(randomizer.code())
                .title(randomizer.name())
                .credits(3)
                .build();

        assertThatThrownBy(() -> updateSyllabusApi.update(999999L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }
}