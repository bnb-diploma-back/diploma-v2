package sdu.edu.kz.diploma.api.weekly.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.library.model.enums.TaskStatus;
import sdu.edu.kz.diploma.library.model.enums.TaskType;
import sdu.edu.kz.diploma.library.test.BaseTest;
import sdu.edu.kz.diploma.library.test.builder.StudentTaskTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetWeeklyApiTest extends BaseTest {

    @Autowired
    private GetWeeklyApi getWeeklyApi;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void findByStudentAndWeek_throwsException_whenStudentNotFound() {
        assertThatThrownBy(() -> getWeeklyApi.findByStudentAndWeek(999999L, 1))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void findByStudentAndWeek_returnsEmptyCourses_whenNoTasks() {
        final var student = creator.student();

        final var result = getWeeklyApi.findByStudentAndWeek(student.getId(), 1);

        assertThat(result.getStudentId()).isEqualTo(student.getId());
        assertThat(result.getStudentFirstName()).isEqualTo(student.getFirstName());
        assertThat(result.getWeekNumber()).isEqualTo(1);
        assertThat(result.getCourses()).isEmpty();
    }

    @Test
    void findByStudentAndWeek_returnsTasksGroupedByCourse() {
        final var student = creator.student();
        final var syllabus1 = creator.syllabus();
        final var syllabus2 = creator.syllabus();

        creator.studentTask(student, syllabus1, 3);
        creator.studentTask(student, syllabus1, 3);
        creator.studentTask(student, syllabus2, 3);

        final var result = getWeeklyApi.findByStudentAndWeek(student.getId(), 3);

        assertThat(result.getCourses()).hasSize(2);

        final var course1 = result.getCourses().stream()
                .filter(c -> c.getSyllabusId().equals(syllabus1.getId()))
                .findFirst().orElseThrow();
        assertThat(course1.getTasks()).hasSize(2);
        assertThat(course1.getCourseCode()).isEqualTo(syllabus1.getCourseCode());
        assertThat(course1.getCourseTitle()).isEqualTo(syllabus1.getTitle());

        final var course2 = result.getCourses().stream()
                .filter(c -> c.getSyllabusId().equals(syllabus2.getId()))
                .findFirst().orElseThrow();
        assertThat(course2.getTasks()).hasSize(1);
    }

    @Test
    void findByStudentAndWeek_doesNotReturnTasksFromOtherWeeks() {
        final var student = creator.student();
        final var syllabus = creator.syllabus();

        creator.studentTask(student, syllabus, 3);
        creator.studentTask(student, syllabus, 5);

        final var result = getWeeklyApi.findByStudentAndWeek(student.getId(), 3);

        assertThat(result.getCourses()).hasSize(1);
        assertThat(result.getCourses().getFirst().getTasks()).hasSize(1);
    }

    @Test
    void findByStudentAndWeek_doesNotReturnTasksFromOtherStudents() {
        final var student1 = creator.student();
        final var student2 = creator.student();
        final var syllabus = creator.syllabus();

        creator.studentTask(student1, syllabus, 3);
        creator.studentTask(student2, syllabus, 3);

        final var result = getWeeklyApi.findByStudentAndWeek(student1.getId(), 3);

        assertThat(result.getCourses()).hasSize(1);
        assertThat(result.getCourses().getFirst().getTasks()).hasSize(1);
    }

    @Test
    void findByStudentAndWeek_returnsExpectedGrade() {
        final var student = creator.student();
        final var syllabus = creator.syllabus();
        creator.studentSyllabus(student, syllabus, "A+");
        creator.studentTask(student, syllabus, 3);

        final var result = getWeeklyApi.findByStudentAndWeek(student.getId(), 3);

        assertThat(result.getCourses().getFirst().getExpectedGrade()).isEqualTo("A+");
    }

    @Test
    void findByStudentAndWeek_returnsTaskDetails() {
        final var student = creator.student();
        final var syllabus = creator.syllabus();

        creator.studentTask(
                new StudentTaskTest()
                        .student(student)
                        .syllabus(syllabus)
                        .weekNumber(1)
                        .title("Midterm Prep")
                        .taskType(TaskType.MIDTERM)
                        .status(TaskStatus.PENDING)
                        .maxScore(100)
        );

        final var result = getWeeklyApi.findByStudentAndWeek(student.getId(), 1);

        final var taskResponse = result.getCourses().getFirst().getTasks().getFirst();
        assertThat(taskResponse.getTitle()).isEqualTo("Midterm Prep");
        assertThat(taskResponse.getTaskType()).isEqualTo("MIDTERM");
        assertThat(taskResponse.getStatus()).isEqualTo("PENDING");
        assertThat(taskResponse.getMaxScore()).isEqualTo(100);
    }
}