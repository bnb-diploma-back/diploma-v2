package sdu.edu.kz.diploma.api.student.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.entity.Student;
import sdu.edu.kz.diploma.library.model.entity.StudentCareer;
import sdu.edu.kz.diploma.library.model.entity.StudentSyllabus;
import sdu.edu.kz.diploma.library.model.repository.StudentRepository;
import sdu.edu.kz.diploma.library.model.repository.SyllabusRepository;

@Service
@RequiredArgsConstructor
public class CreateStudentApi {

    private final StudentRepository studentRepository;
    private final SyllabusRepository syllabusRepository;

    @Transactional
    public Long create(CreateStudentRequest request) {
        final var student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .studentId(request.getStudentId())
                .department(request.getDepartment())
                .major(request.getMajor())
                .enrollmentYear(request.getEnrollmentYear())
                .currentSemester(request.getCurrentSemester())
                .dateOfBirth(request.getDateOfBirth())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();

        if (request.getStudentSyllabi() != null) {
            request.getStudentSyllabi().forEach(ssRequest -> {
                final var syllabus = syllabusRepository.findById(ssRequest.getSyllabusId())
                        .orElseThrow(() -> new RuntimeException("Syllabus not found with id: " + ssRequest.getSyllabusId()));
                final var studentSyllabus = StudentSyllabus.builder()
                        .syllabus(syllabus)
                        .expectedGrade(ssRequest.getExpectedGrade())
                        .notes(ssRequest.getNotes())
                        .build();
                student.addStudentSyllabus(studentSyllabus);
            });
        }

        if (request.getStudentCareers() != null) {
            request.getStudentCareers().forEach(scRequest -> {
                final var studentCareer = StudentCareer.builder()
                        .profession(scRequest.getProfession())
                        .description(scRequest.getDescription())
                        .requiredSkills(scRequest.getRequiredSkills())
                        .build();
                student.addStudentCareer(studentCareer);
            });
        }

        final var saved = studentRepository.save(student);
        return saved.getId();
    }
}