package sdu.edu.kz.diploma.api.syllabus.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.syllabus.entity.Syllabus;
import sdu.edu.kz.diploma.library.model.syllabus.entity.WeeklyPlan;
import sdu.edu.kz.diploma.library.model.syllabus.repository.SyllabusRepository;

@Service
@RequiredArgsConstructor
public class CreateSyllabusApi {

    private final SyllabusRepository syllabusRepository;

    @Transactional
    public Long create(CreateSyllabusRequest request) {
        final var syllabus = Syllabus.builder()
                .courseCode(request.getCourseCode())
                .title(request.getTitle())
                .description(request.getDescription())
                .credits(request.getCredits())
                .department(request.getDepartment())
                .instructor(request.getInstructor())
                .prerequisites(request.getPrerequisites())
                .objectives(request.getObjectives())
                .learningOutcomes(request.getLearningOutcomes())
                .assessmentCriteria(request.getAssessmentCriteria())
                .requiredTextbooks(request.getRequiredTextbooks())
                .recommendedReading(request.getRecommendedReading())
                .academicYear(request.getAcademicYear())
                .semester(request.getSemester())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        if (request.getWeeklyPlans() != null) {
            request.getWeeklyPlans().forEach(planRequest -> {
                final var plan = WeeklyPlan.builder()
                        .weekNumber(planRequest.getWeekNumber())
                        .topic(planRequest.getTopic())
                        .learningObjectives(planRequest.getLearningObjectives())
                        .lectureContent(planRequest.getLectureContent())
                        .practiceContent(planRequest.getPracticeContent())
                        .assignments(planRequest.getAssignments())
                        .readings(planRequest.getReadings())
                        .build();
                syllabus.addWeeklyPlan(plan);
            });
        }

        final var saved = syllabusRepository.save(syllabus);
        return saved.getId();
    }
}