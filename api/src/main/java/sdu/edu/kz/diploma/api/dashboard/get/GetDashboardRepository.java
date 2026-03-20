package sdu.edu.kz.diploma.api.dashboard.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import sdu.edu.kz.diploma.library.jooq.tables.pojos.StudentCareers;
import sdu.edu.kz.diploma.library.jooq.tables.pojos.StudentSyllabi;
import sdu.edu.kz.diploma.library.jooq.tables.pojos.StudentTasks;
import sdu.edu.kz.diploma.library.jooq.tables.pojos.WeeklyOrganizers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static sdu.edu.kz.diploma.library.jooq.Tables.*;

@Repository
@RequiredArgsConstructor
public class GetDashboardRepository {

    private final DSLContext dsl;
    private final ObjectMapper objectMapper;

    public Optional<GetDashboardResponse> findByStudentId(Long studentId, Integer currentWeek) {
        final var student = dsl.selectFrom(STUDENTS)
                .where(STUDENTS.ID.eq(studentId))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Students.class);

        if (student == null) {
            return Optional.empty();
        }

        final var profile = GetDashboardResponse.StudentProfileResponse.builder()
                .id(student.id())
                .firstName(student.firstName())
                .lastName(student.lastName())
                .email(student.email())
                .studentId(student.studentId())
                .department(student.department())
                .major(student.major())
                .enrollmentYear(student.enrollmentYear())
                .currentSemester(student.currentSemester())
                .build();

        final var academicOverview = buildAcademicOverview(studentId);
        final var currentWeekResponse = buildCurrentWeek(studentId, currentWeek);
        final var upcomingDeadlines = buildUpcomingDeadlines(studentId);
        final var taskProgress = buildTaskProgress(studentId);
        final var topCareers = buildTopCareers(studentId);
        final var todaySchedule = buildTodaySchedule(studentId, currentWeek);

        return Optional.of(GetDashboardResponse.builder()
                .profile(profile)
                .academicOverview(academicOverview)
                .currentWeek(currentWeekResponse)
                .upcomingDeadlines(upcomingDeadlines)
                .taskProgress(taskProgress)
                .topCareers(topCareers)
                .todaySchedule(todaySchedule)
                .build());
    }

    private GetDashboardResponse.AcademicOverviewResponse buildAcademicOverview(Long studentId) {
        final var studentSyllabi = dsl.selectFrom(STUDENT_SYLLABI)
                .where(STUDENT_SYLLABI.STUDENT_ID.eq(studentId))
                .fetchInto(StudentSyllabi.class);

        final var courses = studentSyllabi.stream()
                .map(ss -> {
                    final var syllabus = dsl.selectFrom(SYLLABI)
                            .where(SYLLABI.ID.eq(ss.syllabusId()))
                            .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Syllabi.class);

                    return GetDashboardResponse.CourseSnapshotResponse.builder()
                            .syllabusId(syllabus.id())
                            .courseCode(syllabus.courseCode())
                            .courseTitle(syllabus.title())
                            .instructor(syllabus.instructor())
                            .credits(syllabus.credits())
                            .expectedGrade(ss.expectedGrade())
                            .build();
                })
                .toList();

        final var totalCredits = courses.stream()
                .mapToInt(c -> c.getCredits() != null ? c.getCredits() : 0)
                .sum();

        return GetDashboardResponse.AcademicOverviewResponse.builder()
                .totalCourses(courses.size())
                .totalCredits(totalCredits)
                .courses(courses)
                .build();
    }

    private GetDashboardResponse.CurrentWeekResponse buildCurrentWeek(Long studentId, Integer weekNumber) {
        final var tasks = dsl.selectFrom(STUDENT_TASKS)
                .where(STUDENT_TASKS.STUDENT_ID.eq(studentId))
                .and(STUDENT_TASKS.WEEK_NUMBER.eq(weekNumber))
                .fetchInto(StudentTasks.class);

        final var completed = tasks.stream()
                .filter(t -> "GRADED".equals(t.status()) || "SUBMITTED".equals(t.status()))
                .count();
        final var overdue = tasks.stream()
                .filter(t -> "OVERDUE".equals(t.status()))
                .count();
        final var pending = tasks.size() - completed - overdue;

        final var hasOrganizer = dsl.selectFrom(WEEKLY_ORGANIZERS)
                .where(WEEKLY_ORGANIZERS.STUDENT_ID.eq(studentId))
                .and(WEEKLY_ORGANIZERS.WEEK_NUMBER.eq(weekNumber))
                .fetchOne() != null;

        return GetDashboardResponse.CurrentWeekResponse.builder()
                .weekNumber(weekNumber)
                .totalTasks(tasks.size())
                .completedTasks((int) completed)
                .pendingTasks((int) pending)
                .overdueTasks((int) overdue)
                .hasOrganizer(hasOrganizer)
                .build();
    }

    private List<GetDashboardResponse.UpcomingDeadlineResponse> buildUpcomingDeadlines(Long studentId) {
        final var today = LocalDate.now();

        final var tasks = dsl.selectFrom(STUDENT_TASKS)
                .where(STUDENT_TASKS.STUDENT_ID.eq(studentId))
                .and(STUDENT_TASKS.DUE_DATE.greaterOrEqual(today))
                .and(STUDENT_TASKS.STATUS.notIn("GRADED", "SUBMITTED"))
                .orderBy(STUDENT_TASKS.DUE_DATE.asc())
                .limit(10)
                .fetchInto(StudentTasks.class);

        return tasks.stream()
                .map(t -> {
                    final var syllabus = dsl.selectFrom(SYLLABI)
                            .where(SYLLABI.ID.eq(t.syllabusId()))
                            .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Syllabi.class);

                    return GetDashboardResponse.UpcomingDeadlineResponse.builder()
                            .taskId(t.id())
                            .taskTitle(t.title())
                            .courseCode(syllabus != null ? syllabus.courseCode() : null)
                            .courseTitle(syllabus != null ? syllabus.title() : null)
                            .taskType(t.taskType())
                            .status(t.status())
                            .dueDate(t.dueDate())
                            .daysRemaining((int) ChronoUnit.DAYS.between(today, t.dueDate()))
                            .maxScore(t.maxScore())
                            .build();
                })
                .toList();
    }

    private GetDashboardResponse.TaskProgressResponse buildTaskProgress(Long studentId) {
        final var allTasks = dsl.selectFrom(STUDENT_TASKS)
                .where(STUDENT_TASKS.STUDENT_ID.eq(studentId))
                .fetchInto(StudentTasks.class);

        final var pending = allTasks.stream().filter(t -> "PENDING".equals(t.status())).count();
        final var inProgress = allTasks.stream().filter(t -> "IN_PROGRESS".equals(t.status())).count();
        final var submitted = allTasks.stream().filter(t -> "SUBMITTED".equals(t.status())).count();
        final var graded = allTasks.stream().filter(t -> "GRADED".equals(t.status())).count();
        final var overdue = allTasks.stream().filter(t -> "OVERDUE".equals(t.status())).count();

        final var completionPercentage = allTasks.isEmpty() ? 0
                : (int) ((submitted + graded) * 100 / allTasks.size());

        final var averageScore = allTasks.stream()
                .filter(t -> t.score() != null && t.maxScore() != null && t.maxScore() > 0)
                .mapToDouble(t -> (double) t.score() / t.maxScore() * 100)
                .average()
                .orElse(0.0);

        return GetDashboardResponse.TaskProgressResponse.builder()
                .totalTasks(allTasks.size())
                .pendingCount((int) pending)
                .inProgressCount((int) inProgress)
                .submittedCount((int) submitted)
                .gradedCount((int) graded)
                .overdueCount((int) overdue)
                .completionPercentage(completionPercentage)
                .averageScore(Math.round(averageScore * 10.0) / 10.0)
                .build();
    }

    private List<GetDashboardResponse.TopCareerResponse> buildTopCareers(Long studentId) {
        final var careers = dsl.selectFrom(STUDENT_CAREERS)
                .where(STUDENT_CAREERS.STUDENT_ID.eq(studentId))
                .limit(3)
                .fetchInto(StudentCareers.class);

        return careers.stream()
                .map(c -> GetDashboardResponse.TopCareerResponse.builder()
                        .profession(c.profession())
                        .build())
                .toList();
    }

    private GetDashboardResponse.TodayScheduleResponse buildTodaySchedule(Long studentId, Integer weekNumber) {
        final var organizer = dsl.selectFrom(WEEKLY_ORGANIZERS)
                .where(WEEKLY_ORGANIZERS.STUDENT_ID.eq(studentId))
                .and(WEEKLY_ORGANIZERS.WEEK_NUMBER.eq(weekNumber))
                .fetchOneInto(WeeklyOrganizers.class);

        if (organizer == null) {
            return null;
        }

        try {
            final var todayName = LocalDate.now().getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            final var responseNode = objectMapper.readTree(organizer.aiResponse());
            final var dailyPlans = responseNode.get("dailyPlans");

            if (dailyPlans == null || !dailyPlans.isArray()) {
                return null;
            }

            for (final var plan : dailyPlans) {
                if (todayName.equalsIgnoreCase(plan.get("day").asText())) {
                    final var timeBlocks = new ArrayList<GetDashboardResponse.TimeBlockResponse>();

                    if (plan.has("timeBlocks") && plan.get("timeBlocks").isArray()) {
                        for (final var block : plan.get("timeBlocks")) {
                            timeBlocks.add(GetDashboardResponse.TimeBlockResponse.builder()
                                    .startTime(block.has("startTime") ? block.get("startTime").asText() : null)
                                    .endTime(block.has("endTime") ? block.get("endTime").asText() : null)
                                    .taskTitle(block.has("taskTitle") ? block.get("taskTitle").asText() : null)
                                    .courseCode(block.has("courseCode") ? block.get("courseCode").asText() : null)
                                    .activityType(block.has("activityType") ? block.get("activityType").asText() : null)
                                    .effortLevel(block.has("effortLevel") ? block.get("effortLevel").asText() : null)
                                    .focusTip(block.has("focusTip") ? block.get("focusTip").asText() : null)
                                    .estimatedMinutes(block.has("estimatedMinutes") ? block.get("estimatedMinutes").asInt() : null)
                                    .build());
                        }
                    }

                    return GetDashboardResponse.TodayScheduleResponse.builder()
                            .day(todayName)
                            .dailyTip(plan.has("dailyTip") ? plan.get("dailyTip").asText() : null)
                            .timeBlocks(timeBlocks)
                            .build();
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }
}