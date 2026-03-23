package sdu.edu.kz.diploma.api.weekly.get;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import sdu.edu.kz.diploma.library.jooq.tables.pojos.StudentSyllabi;
import sdu.edu.kz.diploma.library.jooq.tables.pojos.StudentTasks;

import java.util.Optional;
import java.util.stream.Collectors;

import static sdu.edu.kz.diploma.library.jooq.Tables.*;

@Repository
@RequiredArgsConstructor
public class GetWeeklyRepository {

    private final DSLContext dsl;

    public Optional<GetWeeklyResponse> findByStudentAndWeek(Long studentId, Integer weekNumber) {
        final var student = dsl.selectFrom(STUDENTS)
                .where(STUDENTS.ID.eq(studentId))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Students.class);

        if (student == null) {
            return Optional.empty();
        }

        final var tasks = dsl.selectFrom(STUDENT_TASKS)
                .where(STUDENT_TASKS.STUDENT_ID.eq(studentId))
                .and(STUDENT_TASKS.WEEK_NUMBER.eq(weekNumber))
                .fetchInto(StudentTasks.class);

        final var tasksBySyllabus = tasks.stream()
                .collect(Collectors.groupingBy(StudentTasks::syllabusId));

        final var courses = tasksBySyllabus.entrySet().stream()
                .map(entry -> {
                    final var syllabus = dsl.selectFrom(SYLLABI)
                            .where(SYLLABI.ID.eq(entry.getKey()))
                            .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Syllabi.class);

                    final var studentSyllabus = dsl.selectFrom(STUDENT_SYLLABI)
                            .where(STUDENT_SYLLABI.STUDENT_ID.eq(studentId))
                            .and(STUDENT_SYLLABI.SYLLABUS_ID.eq(entry.getKey()))
                            .fetchOneInto(StudentSyllabi.class);

                    return GetWeeklyResponse.CourseTasksResponse.builder()
                            .syllabusId(syllabus.id())
                            .courseCode(syllabus.courseCode())
                            .courseTitle(syllabus.title())
                            .department(syllabus.department())
                            .instructor(syllabus.instructor())
                            .credits(syllabus.credits())
                            .semester(syllabus.semester())
                            .expectedGrade(studentSyllabus != null ? studentSyllabus.expectedGrade() : null)
                            .tasks(entry.getValue().stream()
                                    .map(this::toTaskResponse)
                                    .toList())
                            .build();
                })
                .toList();

        return Optional.of(GetWeeklyResponse.builder()
                .studentId(student.id())
                .studentFirstName(student.firstName())
                .studentLastName(student.lastName())
                .weekNumber(weekNumber)
                .courses(courses)
                .build());
    }

    private GetWeeklyResponse.TaskResponse toTaskResponse(StudentTasks t) {
        return GetWeeklyResponse.TaskResponse.builder()
                .id(t.id())
                .title(t.title())
                .description(t.description())
                .instructions(t.instructions())
                .taskType(t.taskType())
                .status(t.status())
                .dueDate(t.dueDate())
                .maxScore(t.maxScore())
                .score(t.score())
                .feedback(t.feedback())
                .submissionUrl(t.submissionUrl())
                .submittedAt(t.submittedAt())
                .createdAt(t.createdAt())
                .build();
    }
}