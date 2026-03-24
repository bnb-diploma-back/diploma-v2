package sdu.edu.kz.diploma.api.student.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import sdu.edu.kz.diploma.library.model.entity.Student;
import sdu.edu.kz.diploma.library.model.entity.StudentCareer;
import sdu.edu.kz.diploma.library.model.entity.StudentSyllabus;
import sdu.edu.kz.diploma.library.model.entity.User;
import sdu.edu.kz.diploma.library.model.repository.DepartmentRepository;
import sdu.edu.kz.diploma.library.model.repository.MajorRepository;
import sdu.edu.kz.diploma.library.model.repository.StudentRepository;
import sdu.edu.kz.diploma.library.model.repository.SyllabusRepository;
import sdu.edu.kz.diploma.library.model.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CreateStudentApi {

    private final StudentRepository studentRepository;
    private final SyllabusRepository syllabusRepository;
    private final DepartmentRepository departmentRepository;
    private final MajorRepository majorRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long create(CreateStudentRequest request) {
        final var department = request.getDepartmentId() != null
                ? departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + request.getDepartmentId()))
                : null;

        final var major = request.getMajorId() != null
                ? majorRepository.findById(request.getMajorId())
                    .orElseThrow(() -> new RuntimeException("Major not found with id: " + request.getMajorId()))
                : null;

        final var student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .studentId(request.getStudentId())
                .department(department)
                .major(major)
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

        bindCurrentUser(saved);

        return saved.getId();
    }

    private void bindCurrentUser(Student student) {
        final var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User currentUser) {
            if (currentUser.getStudent() == null) {
                final var user = userRepository.findById(currentUser.getId()).orElse(null);
                if (user != null) {
                    user.setStudent(student);
                    userRepository.save(user);
                }
            }
        }
    }
}