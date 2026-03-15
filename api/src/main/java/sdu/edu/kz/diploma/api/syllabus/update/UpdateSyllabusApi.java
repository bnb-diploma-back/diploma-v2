package sdu.edu.kz.diploma.api.syllabus.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.syllabus.entity.WeeklyPlan;
import sdu.edu.kz.diploma.library.model.syllabus.repository.SyllabusRepository;

@Service
@RequiredArgsConstructor
public class UpdateSyllabusApi {

    private final SyllabusRepository syllabusRepository;

    @Transactional
    public void update(Long id, UpdateSyllabusRequest request) {
        final var syllabus = syllabusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Syllabus not found with id: " + id));

        syllabus.setCourseCode(request.getCourseCode());
        syllabus.setTitle(request.getTitle());
        syllabus.setDescription(request.getDescription());
        syllabus.setCredits(request.getCredits());
        syllabus.setDepartment(request.getDepartment());
        syllabus.setInstructor(request.getInstructor());
        syllabus.setPrerequisites(request.getPrerequisites());
        syllabus.setObjectives(request.getObjectives());
        syllabus.setLearningOutcomes(request.getLearningOutcomes());
        syllabus.setAssessmentCriteria(request.getAssessmentCriteria());
        syllabus.setRequiredTextbooks(request.getRequiredTextbooks());
        syllabus.setRecommendedReading(request.getRecommendedReading());
        syllabus.setAcademicYear(request.getAcademicYear());
        syllabus.setSemester(request.getSemester());
        syllabus.setStartDate(request.getStartDate());
        syllabus.setEndDate(request.getEndDate());

        syllabus.getWeeklyPlans().clear();
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

        syllabusRepository.save(syllabus);
    }
}