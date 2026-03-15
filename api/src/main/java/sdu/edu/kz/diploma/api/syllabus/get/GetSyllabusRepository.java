package sdu.edu.kz.diploma.api.syllabus.get;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;
import sdu.edu.kz.diploma.library.jooq.tables.pojos.WeeklyPlans;

import java.util.List;
import java.util.Optional;

import static sdu.edu.kz.diploma.library.jooq.Tables.SYLLABI;
import static sdu.edu.kz.diploma.library.jooq.Tables.WEEKLY_PLANS;

@Repository
@RequiredArgsConstructor
public class GetSyllabusRepository {

    private final DSLContext dsl;

    public List<GetSyllabusResponse> findAll() {
        final var syllabi = dsl.selectFrom(SYLLABI)
                .orderBy(SYLLABI.ID)
                .fetchInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Syllabi.class);

        return syllabi.stream()
                .map(s -> toResponse(s, fetchWeeklyPlans(s.id())))
                .toList();
    }

    public Optional<GetSyllabusResponse> findById(Long id) {
        final var syllabus = dsl.selectFrom(SYLLABI)
                .where(SYLLABI.ID.eq(id))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Syllabi.class);

        if (syllabus == null) {
            return Optional.empty();
        }
        return Optional.of(toResponse(syllabus, fetchWeeklyPlans(id)));
    }

    public Optional<GetSyllabusResponse> findByCourseCode(String courseCode) {
        final var syllabus = dsl.selectFrom(SYLLABI)
                .where(SYLLABI.COURSE_CODE.eq(courseCode))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Syllabi.class);

        if (syllabus == null) {
            return Optional.empty();
        }
        return Optional.of(toResponse(syllabus, fetchWeeklyPlans(syllabus.id())));
    }

    private List<WeeklyPlans> fetchWeeklyPlans(Long syllabusId) {
        return dsl.selectFrom(WEEKLY_PLANS)
                .where(WEEKLY_PLANS.SYLLABUS_ID.eq(syllabusId))
                .orderBy(WEEKLY_PLANS.WEEK_NUMBER)
                .fetchInto(WeeklyPlans.class);
    }

    private GetSyllabusResponse toResponse(sdu.edu.kz.diploma.library.jooq.tables.pojos.Syllabi s, List<WeeklyPlans> plans) {
        return GetSyllabusResponse.builder()
                .id(s.id())
                .courseCode(s.courseCode())
                .title(s.title())
                .description(s.description())
                .credits(s.credits())
                .department(s.department())
                .instructor(s.instructor())
                .prerequisites(s.prerequisites())
                .objectives(s.objectives())
                .learningOutcomes(s.learningOutcomes())
                .assessmentCriteria(s.assessmentCriteria())
                .requiredTextbooks(s.requiredTextbooks())
                .recommendedReading(s.recommendedReading())
                .academicYear(s.academicYear())
                .semester(s.semester())
                .startDate(s.startDate())
                .endDate(s.endDate())
                .weeklyPlans(plans.stream()
                        .map(p -> GetSyllabusResponse.WeeklyPlanResponse.builder()
                                .id(p.id())
                                .weekNumber(p.weekNumber())
                                .topic(p.topic())
                                .learningObjectives(p.learningObjectives())
                                .lectureContent(p.lectureContent())
                                .practiceContent(p.practiceContent())
                                .assignments(p.assignments())
                                .readings(p.readings())
                                .build())
                        .toList())
                .createdAt(s.createdAt())
                .updatedAt(s.updatedAt())
                .build();
    }
}