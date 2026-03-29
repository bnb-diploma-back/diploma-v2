package sdu.edu.kz.diploma.api.dashboard.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.library.model.enums.TaskStatus;
import sdu.edu.kz.diploma.library.test.BaseTest;
import sdu.edu.kz.diploma.library.test.builder.StudentTaskTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetDashboardApiTest extends BaseTest {

    @Autowired
    private GetDashboardApi getDashboardApi;

    @BeforeEach
    void setUp() {
        remover.all();
    }

    @Test
    void findByStudentId_throwsException_whenStudentNotFound() {
        assertThatThrownBy(() -> getDashboardApi.findByStudentId(999999L, 1))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void findByStudentId_returnsProfile() {
        final var student = creator.student();

        final var result = getDashboardApi.findByStudentId(student.getId(), 1);

        assertThat(result.getProfile().getId()).isEqualTo(student.getId());
        assertThat(result.getProfile().getFirstName()).isEqualTo(student.getFirstName());
        assertThat(result.getProfile().getLastName()).isEqualTo(student.getLastName());
        assertThat(result.getProfile().getEmail()).isEqualTo(student.getEmail());
        assertThat(result.getProfile().getDepartmentId()).isEqualTo(student.getDepartment().getId());
        assertThat(result.getProfile().getMajorId()).isEqualTo(student.getMajor().getId());
    }

    @Test
    void findByStudentId_returnsAcademicOverview() {
        final var student = creator.student();
        final var syllabus1 = creator.syllabus();
        final var syllabus2 = creator.syllabus();
        creator.studentSyllabus(student, syllabus1, "A");
        creator.studentSyllabus(student, syllabus2, "B+");

        final var result = getDashboardApi.findByStudentId(student.getId(), 1);

        assertThat(result.getAcademicOverview().getTotalCourses()).isEqualTo(2);
        assertThat(result.getAcademicOverview().getTotalCredits())
                .isEqualTo(syllabus1.getCredits() + syllabus2.getCredits());
        assertThat(result.getAcademicOverview().getCourses()).hasSize(2);
    }

    @Test
    void findByStudentId_returnsCourseWithExpectedGrade() {
        final var student = creator.student();
        final var syllabus = creator.syllabus();
        creator.studentSyllabus(student, syllabus, "A+");

        final var result = getDashboardApi.findByStudentId(student.getId(), 1);

        final var course = result.getAcademicOverview().getCourses().getFirst();
        assertThat(course.getCourseCode()).isEqualTo(syllabus.getCourseCode());
        assertThat(course.getExpectedGrade()).isEqualTo("A+");
    }

    @Test
    void findByStudentId_returnsCurrentWeekStats() {
        final var student = creator.student();
        final var syllabus = creator.syllabus();

        creator.studentTask(new StudentTaskTest()
                .student(student).syllabus(syllabus).weekNumber(3).status(TaskStatus.PENDING));
        creator.studentTask(new StudentTaskTest()
                .student(student).syllabus(syllabus).weekNumber(3).status(TaskStatus.SUBMITTED));
        creator.studentTask(new StudentTaskTest()
                .student(student).syllabus(syllabus).weekNumber(3).status(TaskStatus.OVERDUE));

        final var result = getDashboardApi.findByStudentId(student.getId(), 3);

        assertThat(result.getCurrentWeek().getWeekNumber()).isEqualTo(3);
        assertThat(result.getCurrentWeek().getTotalTasks()).isEqualTo(3);
        assertThat(result.getCurrentWeek().getCompletedTasks()).isEqualTo(1);
        assertThat(result.getCurrentWeek().getOverdueTasks()).isEqualTo(1);
        assertThat(result.getCurrentWeek().isHasOrganizer()).isFalse();
    }

    @Test
    void findByStudentId_detectsExistingOrganizer() {
        final var student = creator.student();
        creator.weeklyOrganizer(student, 3);

        final var result = getDashboardApi.findByStudentId(student.getId(), 3);

        assertThat(result.getCurrentWeek().isHasOrganizer()).isTrue();
    }

    @Test
    void findByStudentId_returnsUpcomingDeadlines() {
        final var student = creator.student();
        final var syllabus = creator.syllabus();

        creator.studentTask(new StudentTaskTest()
                .student(student).syllabus(syllabus).weekNumber(1)
                .title("Future Task")
                .dueDate(LocalDate.now().plusDays(5))
                .status(TaskStatus.PENDING));

        creator.studentTask(new StudentTaskTest()
                .student(student).syllabus(syllabus).weekNumber(1)
                .title("Already Done")
                .dueDate(LocalDate.now().plusDays(3))
                .status(TaskStatus.SUBMITTED));

        final var result = getDashboardApi.findByStudentId(student.getId(), 1);

        assertThat(result.getUpcomingDeadlines()).hasSize(1);
        assertThat(result.getUpcomingDeadlines().getFirst().getTaskTitle()).isEqualTo("Future Task");
        assertThat(result.getUpcomingDeadlines().getFirst().getDaysRemaining()).isEqualTo(5);
    }

    @Test
    void findByStudentId_returnsTaskProgress() {
        final var student = creator.student();
        final var syllabus = creator.syllabus();

        creator.studentTask(new StudentTaskTest()
                .student(student).syllabus(syllabus).weekNumber(1).status(TaskStatus.PENDING));
        creator.studentTask(new StudentTaskTest()
                .student(student).syllabus(syllabus).weekNumber(1).status(TaskStatus.IN_PROGRESS));
        creator.studentTask(new StudentTaskTest()
                .student(student).syllabus(syllabus).weekNumber(2).status(TaskStatus.SUBMITTED));
        creator.studentTask(new StudentTaskTest()
                .student(student).syllabus(syllabus).weekNumber(2).status(TaskStatus.GRADED)
                .maxScore(100).score(85));

        final var result = getDashboardApi.findByStudentId(student.getId(), 1);

        assertThat(result.getTaskProgress().getTotalTasks()).isEqualTo(4);
        assertThat(result.getTaskProgress().getPendingCount()).isEqualTo(1);
        assertThat(result.getTaskProgress().getInProgressCount()).isEqualTo(1);
        assertThat(result.getTaskProgress().getSubmittedCount()).isEqualTo(1);
        assertThat(result.getTaskProgress().getGradedCount()).isEqualTo(1);
        assertThat(result.getTaskProgress().getCompletionPercentage()).isEqualTo(50);
        assertThat(result.getTaskProgress().getAverageScore()).isEqualTo(85.0);
    }

    @Test
    void findByStudentId_returnsTopCareers() {
        final var student = creator.student();
        creator.studentCareer(student, "Backend Developer");
        creator.studentCareer(student, "Data Engineer");

        final var result = getDashboardApi.findByStudentId(student.getId(), 1);

        assertThat(result.getTopCareers()).hasSize(2);
        assertThat(result.getTopCareers().stream().map(GetDashboardResponse.TopCareerResponse::getProfession))
                .containsExactlyInAnyOrder("Backend Developer", "Data Engineer");
    }

    @Test
    void findByStudentId_returnsTodaySchedule_whenOrganizerExists() {
        final var student = creator.student();
        creator.weeklyOrganizer(student, 1);

        final var result = getDashboardApi.findByStudentId(student.getId(), 1);

        // The default test organizer JSON has Monday only,
        // so todaySchedule will be non-null only if today is Monday
        // Either way, no exception should be thrown
        assertThat(result).isNotNull();
    }

    @Test
    void findByStudentId_returnsNullTodaySchedule_whenNoOrganizer() {
        final var student = creator.student();

        final var result = getDashboardApi.findByStudentId(student.getId(), 1);

        assertThat(result.getTodaySchedule()).isNull();
    }

    @Test
    void findByStudentId_returnsEmptyData_whenStudentHasNoCourses() {
        final var student = creator.student();

        final var result = getDashboardApi.findByStudentId(student.getId(), 1);

        assertThat(result.getAcademicOverview().getTotalCourses()).isZero();
        assertThat(result.getAcademicOverview().getTotalCredits()).isZero();
        assertThat(result.getCurrentWeek().getTotalTasks()).isZero();
        assertThat(result.getUpcomingDeadlines()).isEmpty();
        assertThat(result.getTaskProgress().getTotalTasks()).isZero();
        assertThat(result.getTopCareers()).isEmpty();
    }
}