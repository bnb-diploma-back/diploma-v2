package sdu.edu.kz.diploma.library.test.builder;

import sdu.edu.kz.diploma.library.model.entity.Department;
import sdu.edu.kz.diploma.library.model.entity.Major;
import sdu.edu.kz.diploma.library.model.enums.Semester;
import sdu.edu.kz.diploma.library.model.entity.Syllabus;
import sdu.edu.kz.diploma.library.test.Randomizer;

import java.time.LocalDate;
import java.util.stream.IntStream;

public class SyllabusTest {

    private static final Randomizer r = new Randomizer();

    private String courseCode = r.code();
    private String title = r.name();
    private String description = r.text();
    private int credits = r.intBetween(1, 6);
    private Department department;
    private Major major;
    private String instructor = r.name();
    private String prerequisites = r.text();
    private String objectives = r.text();
    private String learningOutcomes = r.text();
    private String assessmentCriteria = r.text();
    private String requiredTextbooks = r.text();
    private String recommendedReading = r.text();
    private String academicYear = "2025-2026";
    private Semester semester = r.semester();
    private LocalDate startDate = r.date();
    private LocalDate endDate = r.futureDate();
    private int weeklyPlanCount = 0;

    public SyllabusTest courseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }

    public SyllabusTest title(String title) {
        this.title = title;
        return this;
    }

    public SyllabusTest description(String description) {
        this.description = description;
        return this;
    }

    public SyllabusTest credits(int credits) {
        this.credits = credits;
        return this;
    }

    public SyllabusTest department(Department department) {
        this.department = department;
        return this;
    }

    public SyllabusTest major(Major major) {
        this.major = major;
        return this;
    }

    public SyllabusTest instructor(String instructor) {
        this.instructor = instructor;
        return this;
    }

    public SyllabusTest semester(Semester semester) {
        this.semester = semester;
        return this;
    }

    public SyllabusTest academicYear(String academicYear) {
        this.academicYear = academicYear;
        return this;
    }

    public SyllabusTest startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public SyllabusTest endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public SyllabusTest withWeeklyPlans(int count) {
        this.weeklyPlanCount = count;
        return this;
    }

    public SyllabusTest withFullWeeklyPlans() {
        return withWeeklyPlans(15);
    }

    public Syllabus build() {
        final var syllabus = Syllabus.builder()
                .courseCode(courseCode)
                .title(title)
                .description(description)
                .credits(credits)
                .department(department)
                .major(major)
                .instructor(instructor)
                .prerequisites(prerequisites)
                .objectives(objectives)
                .learningOutcomes(learningOutcomes)
                .assessmentCriteria(assessmentCriteria)
                .requiredTextbooks(requiredTextbooks)
                .recommendedReading(recommendedReading)
                .academicYear(academicYear)
                .semester(semester)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        IntStream.rangeClosed(1, weeklyPlanCount).forEach(week ->
                syllabus.addWeeklyPlan(new WeeklyPlanTest().weekNumber(week).build())
        );

        return syllabus;
    }
}