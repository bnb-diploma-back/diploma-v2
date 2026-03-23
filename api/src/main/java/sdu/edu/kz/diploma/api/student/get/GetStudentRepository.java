package sdu.edu.kz.diploma.api.student.get;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import sdu.edu.kz.diploma.library.jooq.tables.pojos.StudentCareers;
import sdu.edu.kz.diploma.library.jooq.tables.pojos.StudentSyllabi;

import java.util.List;
import java.util.Optional;

import static sdu.edu.kz.diploma.library.jooq.Tables.*;

@Repository
@RequiredArgsConstructor
public class GetStudentRepository {

    private final DSLContext dsl;

    public List<GetStudentResponse> findAll() {
        final var students = dsl.selectFrom(STUDENTS)
                .orderBy(STUDENTS.ID)
                .fetchInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Students.class);

        return students.stream()
                .map(s -> toResponse(s, fetchStudentSyllabi(s.id()), fetchStudentCareers(s.id())))
                .toList();
    }

    public Optional<GetStudentResponse> findById(Long id) {
        final var student = dsl.selectFrom(STUDENTS)
                .where(STUDENTS.ID.eq(id))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Students.class);

        if (student == null) {
            return Optional.empty();
        }
        return Optional.of(toResponse(student, fetchStudentSyllabi(id), fetchStudentCareers(id)));
    }

    public Optional<GetStudentResponse> findByStudentId(String studentId) {
        final var student = dsl.selectFrom(STUDENTS)
                .where(STUDENTS.STUDENT_ID.eq(studentId))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Students.class);

        if (student == null) {
            return Optional.empty();
        }
        return Optional.of(toResponse(student, fetchStudentSyllabi(student.id()), fetchStudentCareers(student.id())));
    }

    private List<StudentSyllabi> fetchStudentSyllabi(Long studentId) {
        return dsl.selectFrom(STUDENT_SYLLABI)
                .where(STUDENT_SYLLABI.STUDENT_ID.eq(studentId))
                .fetchInto(StudentSyllabi.class);
    }

    private List<StudentCareers> fetchStudentCareers(Long studentId) {
        return dsl.selectFrom(STUDENT_CAREERS)
                .where(STUDENT_CAREERS.STUDENT_ID.eq(studentId))
                .fetchInto(StudentCareers.class);
    }

    private String fetchDepartmentName(Long departmentId) {
        final var dept = dsl.selectFrom(DEPARTMENTS)
                .where(DEPARTMENTS.ID.eq(departmentId))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Departments.class);
        return dept != null ? dept.name() : null;
    }

    private String fetchMajorName(Long majorId) {
        final var major = dsl.selectFrom(MAJORS)
                .where(MAJORS.ID.eq(majorId))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Majors.class);
        return major != null ? major.name() : null;
    }

    private GetStudentResponse toResponse(sdu.edu.kz.diploma.library.jooq.tables.pojos.Students s,
                                          List<StudentSyllabi> syllabi,
                                          List<StudentCareers> careers) {
        return GetStudentResponse.builder()
                .id(s.id())
                .firstName(s.firstName())
                .lastName(s.lastName())
                .email(s.email())
                .studentId(s.studentId())
                .departmentId(s.departmentId())
                .departmentName(s.departmentId() != null ? fetchDepartmentName(s.departmentId()) : null)
                .majorId(s.majorId())
                .majorName(s.majorId() != null ? fetchMajorName(s.majorId()) : null)
                .enrollmentYear(s.enrollmentYear())
                .currentSemester(s.currentSemester())
                .dateOfBirth(s.dateOfBirth())
                .phone(s.phone())
                .address(s.address())
                .studentSyllabi(syllabi.stream()
                        .map(ss -> {
                            final var syllabus = dsl.selectFrom(SYLLABI)
                                    .where(SYLLABI.ID.eq(ss.syllabusId()))
                                    .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Syllabi.class);
                            return GetStudentResponse.StudentSyllabusResponse.builder()
                                    .id(ss.id())
                                    .syllabusId(ss.syllabusId())
                                    .syllabusTitle(syllabus != null ? syllabus.title() : null)
                                    .syllabusCourseCode(syllabus != null ? syllabus.courseCode() : null)
                                    .expectedGrade(ss.expectedGrade())
                                    .notes(ss.notes())
                                    .build();
                        })
                        .toList())
                .studentCareers(careers.stream()
                        .map(sc -> GetStudentResponse.StudentCareerResponse.builder()
                                .id(sc.id())
                                .profession(sc.profession())
                                .description(sc.description())
                                .requiredSkills(sc.requiredSkills())
                                .build())
                        .toList())
                .createdAt(s.createdAt())
                .updatedAt(s.updatedAt())
                .build();
    }
}