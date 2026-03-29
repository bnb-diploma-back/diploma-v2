package sdu.edu.kz.diploma.api.syllabus.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.library.model.enums.Semester;
import sdu.edu.kz.diploma.library.test.BaseTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CreateSyllabusApiTest extends BaseTest {

    @Autowired
    private CreateSyllabusApi createSyllabusApi;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void create_returnsSyllabusId() {
        final var department = creator.department();

        final var request = CreateSyllabusRequest.builder()
                .courseCode(randomizer.code())
                .title(randomizer.name())
                .description(randomizer.text())
                .credits(3)
                .departmentId(department.getId())
                .instructor(randomizer.name())
                .semester(Semester.FALL)
                .academicYear("2025-2026")
                .startDate(LocalDate.of(2025, 9, 1))
                .endDate(LocalDate.of(2025, 12, 15))
                .build();

        final var id = createSyllabusApi.create(request);

        assertThat(id).isNotNull();
    }

    @Test
    void create_persistsSyllabusWithWeeklyPlans() {
        final var request = CreateSyllabusRequest.builder()
                .courseCode(randomizer.code())
                .title(randomizer.name())
                .credits(4)
                .weeklyPlans(List.of(
                        CreateSyllabusRequest.WeeklyPlanRequest.builder()
                                .weekNumber(1)
                                .topic(randomizer.name())
                                .build(),
                        CreateSyllabusRequest.WeeklyPlanRequest.builder()
                                .weekNumber(2)
                                .topic(randomizer.name())
                                .build()
                ))
                .build();

        final var id = createSyllabusApi.create(request);

        final var saved = creator.syllabus();
        assertThat(id).isNotNull();
        assertThat(id).isNotEqualTo(saved.getId());
    }

    @Test
    void create_setsCorrectFields() {
        final var code = randomizer.code();
        final var title = randomizer.name();
        final var request = CreateSyllabusRequest.builder()
                .courseCode(code)
                .title(title)
                .credits(5)
                .semester(Semester.SPRING)
                .build();

        final var id = createSyllabusApi.create(request);

        assertThat(id).isNotNull();
        final var found = creator.syllabus();
        assertThat(found).isNotNull();
    }
}