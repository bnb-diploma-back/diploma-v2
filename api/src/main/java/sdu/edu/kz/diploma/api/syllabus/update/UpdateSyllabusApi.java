package sdu.edu.kz.diploma.api.syllabus.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.entity.WeeklyPlan;
import sdu.edu.kz.diploma.library.model.repository.DepartmentRepository;
import sdu.edu.kz.diploma.library.model.repository.MajorRepository;
import sdu.edu.kz.diploma.library.model.repository.SyllabusRepository;

@Service
@RequiredArgsConstructor
public class UpdateSyllabusApi {

    private final SyllabusRepository syllabusRepository;
    private final DepartmentRepository departmentRepository;
    private final MajorRepository majorRepository;

    @Transactional
    public void update(Long id, UpdateSyllabusRequest request) {
        final var syllabus = syllabusRepository.findById(id)
                .orElseThrow(() -> new sdu.edu.kz.diploma.api.exception.NotFoundException("Syllabus not found with id: " + id));

        final var department = request.getDepartmentId() != null
                ? departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new sdu.edu.kz.diploma.api.exception.NotFoundException("Department not found with id: " + request.getDepartmentId()))
                : null;

        final var major = request.getMajorId() != null
                ? majorRepository.findById(request.getMajorId())
                    .orElseThrow(() -> new sdu.edu.kz.diploma.api.exception.NotFoundException("Major not found with id: " + request.getMajorId()))
                : null;

        syllabus.setCourseCode(request.getCourseCode());
        syllabus.setTitle(request.getTitle());
        syllabus.setDescription(request.getDescription());
        syllabus.setCredits(request.getCredits());
        syllabus.setDepartment(department);
        syllabus.setMajor(major);
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