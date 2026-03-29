package sdu.edu.kz.diploma.library.test.builder;

import sdu.edu.kz.diploma.library.model.entity.*;
import sdu.edu.kz.diploma.library.model.enums.TaskStatus;
import sdu.edu.kz.diploma.library.model.enums.TaskType;
import sdu.edu.kz.diploma.library.test.Randomizer;

import java.time.LocalDate;

public class StudentTaskTest {

    private static final Randomizer r = new Randomizer();

    private Student student;
    private Syllabus syllabus;
    private int weekNumber = r.intBetween(1, 15);
    private String title = r.name();
    private String description = r.text();
    private String instructions = r.text();
    private TaskType taskType = r.oneOf(TaskType.values());
    private TaskStatus status = TaskStatus.PENDING;
    private LocalDate dueDate = r.futureDate();
    private int estimatedHours = r.intBetween(1, 10);
    private int maxScore = r.oneOf(10, 20, 50, 100);
    private Integer score;
    private String feedback;

    public StudentTaskTest student(Student student) {
        this.student = student;
        return this;
    }

    public StudentTaskTest syllabus(Syllabus syllabus) {
        this.syllabus = syllabus;
        return this;
    }

    public StudentTaskTest weekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
        return this;
    }

    public StudentTaskTest title(String title) {
        this.title = title;
        return this;
    }

    public StudentTaskTest description(String description) {
        this.description = description;
        return this;
    }

    public StudentTaskTest instructions(String instructions) {
        this.instructions = instructions;
        return this;
    }

    public StudentTaskTest taskType(TaskType taskType) {
        this.taskType = taskType;
        return this;
    }

    public StudentTaskTest status(TaskStatus status) {
        this.status = status;
        return this;
    }

    public StudentTaskTest dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public StudentTaskTest estimatedHours(int estimatedHours) {
        this.estimatedHours = estimatedHours;
        return this;
    }

    public StudentTaskTest maxScore(int maxScore) {
        this.maxScore = maxScore;
        return this;
    }

    public StudentTaskTest score(int score) {
        this.score = score;
        return this;
    }

    public StudentTaskTest feedback(String feedback) {
        this.feedback = feedback;
        return this;
    }

    public StudentTask build() {
        return StudentTask.builder()
                .student(student)
                .syllabus(syllabus)
                .weekNumber(weekNumber)
                .title(title)
                .description(description)
                .instructions(instructions)
                .taskType(taskType)
                .status(status)
                .dueDate(dueDate)
                .estimatedHours(estimatedHours)
                .maxScore(maxScore)
                .score(score)
                .feedback(feedback)
                .build();
    }
}