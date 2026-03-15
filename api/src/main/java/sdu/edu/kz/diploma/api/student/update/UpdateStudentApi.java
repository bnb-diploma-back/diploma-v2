package sdu.edu.kz.diploma.api.student.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.entity.StudentCareer;
import sdu.edu.kz.diploma.library.model.entity.StudentSyllabus;
import sdu.edu.kz.diploma.library.model.repository.StudentRepository;
import sdu.edu.kz.diploma.library.model.repository.SyllabusRepository;

@Service
@RequiredArgsConstructor
public class UpdateStudentApi {

    private final StudentRepository studentRepository;
    private final SyllabusRepository syllabusRepository;

    @Transactional
    public void update(Long id, UpdateStudentRequest request) {
        final var student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setStudentId(request.getStudentId());
        student.setDepartment(request.getDepartment());
        student.setMajor(request.getMajor());
        student.setEnrollmentYear(request.getEnrollmentYear());
        student.setCurrentSemester(request.getCurrentSemester());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setPhone(request.getPhone());
        student.setAddress(request.getAddress());

        student.getStudentSyllabi().clear();
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

        student.getStudentCareers().clear();
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

        studentRepository.save(student);
    }
}