package sdu.edu.kz.diploma.library.test.builder;

import sdu.edu.kz.diploma.library.model.entity.Student;
import sdu.edu.kz.diploma.library.model.entity.WeeklyOrganizer;
import sdu.edu.kz.diploma.library.test.Randomizer;

public class WeeklyOrganizerTest {

    private static final Randomizer r = new Randomizer();

    private Student student;
    private int weekNumber = r.intBetween(1, 15);
    private String aiResponse = """
            {
              "weeklySummary": "Test weekly summary",
              "dailyPlans": [
                {
                  "day": "Monday",
                  "timeBlocks": [
                    {
                      "startTime": "09:00",
                      "endTime": "10:30",
                      "taskTitle": "Test task",
                      "courseCode": "CS101",
                      "courseName": "Test Course",
                      "activityType": "DEEP_WORK",
                      "effortLevel": "HIGH_FOCUS",
                      "focusTip": "Stay focused",
                      "estimatedMinutes": 90
                    }
                  ],
                  "dailyTip": "Start strong"
                }
              ],
              "wellBeing": {
                "activeRestSuggestions": ["Take a walk"],
                "breakStrategies": ["Pomodoro 25/5"],
                "nutritionTips": ["Stay hydrated"],
                "sleepRecommendations": ["Sleep by 22:00"],
                "mindfulnessTips": ["Breathe deeply"],
                "overallAdvice": "Balance is key"
              }
            }
            """;

    public WeeklyOrganizerTest student(Student student) {
        this.student = student;
        return this;
    }

    public WeeklyOrganizerTest weekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
        return this;
    }

    public WeeklyOrganizerTest aiResponse(String aiResponse) {
        this.aiResponse = aiResponse;
        return this;
    }

    public WeeklyOrganizer build() {
        return WeeklyOrganizer.builder()
                .student(student)
                .weekNumber(weekNumber)
                .aiResponse(aiResponse)
                .build();
    }
}