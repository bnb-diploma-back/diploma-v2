package sdu.edu.kz.diploma.api.syllabus.update;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static sdu.edu.kz.diploma.library.jooq.Tables.SYLLABI;
import static sdu.edu.kz.diploma.library.jooq.Tables.WEEKLY_PLANS;

@Repository
@RequiredArgsConstructor
public class UpdateSyllabusRepository {

    private final DSLContext dsl;

    public boolean existsById(Long id) {
        return dsl.fetchExists(
                dsl.selectOne().from(SYLLABI).where(SYLLABI.ID.eq(id))
        );
    }

    public void updateSyllabus(Long id, UpdateSyllabusRequest request) {
        dsl.update(SYLLABI)
                .set(SYLLABI.COURSE_CODE, request.getCourseCode())
                .set(SYLLABI.TITLE, request.getTitle())
                .set(SYLLABI.DESCRIPTION, request.getDescription())
                .set(SYLLABI.CREDITS, request.getCredits())
                .set(SYLLABI.DEPARTMENT, request.getDepartment())
                .set(SYLLABI.INSTRUCTOR, request.getInstructor())
                .set(SYLLABI.PREREQUISITES, request.getPrerequisites())
                .set(SYLLABI.OBJECTIVES, request.getObjectives())
                .set(SYLLABI.LEARNING_OUTCOMES, request.getLearningOutcomes())
                .set(SYLLABI.ASSESSMENT_CRITERIA, request.getAssessmentCriteria())
                .set(SYLLABI.REQUIRED_TEXTBOOKS, request.getRequiredTextbooks())
                .set(SYLLABI.RECOMMENDED_READING, request.getRecommendedReading())
                .set(SYLLABI.ACADEMIC_YEAR, request.getAcademicYear())
                .set(SYLLABI.SEMESTER, request.getSemester() != null ? request.getSemester().name() : null)
                .set(SYLLABI.START_DATE, request.getStartDate())
                .set(SYLLABI.END_DATE, request.getEndDate())
                .set(SYLLABI.UPDATED_AT, LocalDateTime.now())
                .where(SYLLABI.ID.eq(id))
                .execute();

        dsl.deleteFrom(WEEKLY_PLANS)
                .where(WEEKLY_PLANS.SYLLABUS_ID.eq(id))
                .execute();

        if (request.getWeeklyPlans() != null) {
            insertWeeklyPlans(id, request.getWeeklyPlans());
        }
    }

    private void insertWeeklyPlans(Long syllabusId, List<UpdateSyllabusRequest.WeeklyPlanRequest> plans) {
        final var insert = dsl.insertInto(WEEKLY_PLANS,
                WEEKLY_PLANS.SYLLABUS_ID,
                WEEKLY_PLANS.WEEK_NUMBER,
                WEEKLY_PLANS.TOPIC,
                WEEKLY_PLANS.LEARNING_OBJECTIVES,
                WEEKLY_PLANS.LECTURE_CONTENT,
                WEEKLY_PLANS.PRACTICE_CONTENT,
                WEEKLY_PLANS.ASSIGNMENTS,
                WEEKLY_PLANS.READINGS);

        for (var plan : plans) {
            insert.values(
                    syllabusId,
                    plan.getWeekNumber(),
                    plan.getTopic(),
                    plan.getLearningObjectives(),
                    plan.getLectureContent(),
                    plan.getPracticeContent(),
                    plan.getAssignments(),
                    plan.getReadings()
            );
        }

        insert.execute();
    }
}