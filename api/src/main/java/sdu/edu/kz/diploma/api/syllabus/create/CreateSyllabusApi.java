package sdu.edu.kz.diploma.api.syllabus.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.entity.Syllabus;
import sdu.edu.kz.diploma.library.model.entity.WeeklyPlan;
import sdu.edu.kz.diploma.library.model.repository.DepartmentRepository;
import sdu.edu.kz.diploma.library.model.repository.MajorRepository;
import sdu.edu.kz.diploma.library.model.repository.SyllabusRepository;

@Service
@RequiredArgsConstructor
public class CreateSyllabusApi {

    private final SyllabusRepository syllabusRepository;
    private final DepartmentRepository departmentRepository;
    private final MajorRepository majorRepository;

    @Transactional
    public Long create(CreateSyllabusRequest request) {
        final var department = request.getDepartmentId() != null
                ? departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + request.getDepartmentId()))
                : null;

        final var major = request.getMajorId() != null
                ? majorRepository.findById(request.getMajorId())
                    .orElseThrow(() -> new RuntimeException("Major not found with id: " + request.getMajorId()))
                : null;

        final var syllabus = Syllabus.builder()
                .courseCode(request.getCourseCode())
                .title(request.getTitle())
                .description(request.getDescription())
                .credits(request.getCredits())
                .department(department)
                .major(major)
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