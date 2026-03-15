package sdu.edu.kz.diploma.library.test.builder;

import sdu.edu.kz.diploma.library.model.entity.WeeklyPlan;
import sdu.edu.kz.diploma.library.test.Randomizer;

public class WeeklyPlanTest {

    private static final Randomizer r = new Randomizer();

    private int weekNumber = r.intBetween(1, 15);
    private String topic = r.name();
    private String learningObjectives = r.text();
    private String lectureContent = r.text();
    private String practiceContent = r.text();
    private String assignments = r.text();
    private String readings = r.text();

    public WeeklyPlanTest weekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
        return this;
    }

    public WeeklyPlanTest topic(String topic) {
        this.topic = topic;
        return this;
    }

    public WeeklyPlanTest learningObjectives(String learningObjectives) {
        this.learningObjectives = learningObjectives;
        return this;
    }

    public WeeklyPlanTest lectureContent(String lectureContent) {
        this.lectureContent = lectureContent;
        return this;
    }

    public WeeklyPlanTest practiceContent(String practiceContent) {
        this.practiceContent = practiceContent;
        return this;
    }

    public WeeklyPlanTest assignments(String assignments) {
        this.assignments = assignments;
        return this;
    }

    public WeeklyPlanTest readings(String readings) {
        this.readings = readings;
        return this;
    }

    public WeeklyPlan build() {
        return WeeklyPlan.builder()
                .weekNumber(weekNumber)
                .topic(topic)
                .learningObjectives(learningObjectives)
                .lectureContent(lectureContent)
                .practiceContent(practiceContent)
                .assignments(assignments)
                .readings(readings)
                .build();
    }
}