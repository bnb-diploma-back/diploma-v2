package sdu.edu.kz.diploma.api.weekly.organize;

import tools.jackson.databind.ObjectMapper;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionSystemMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sdu.edu.kz.diploma.api.weekly.get.GetWeeklyResponse;

import java.util.List;

@Service
public class WeeklyAiService {

    private final ObjectMapper objectMapper;
    private final String openaiApiKey;

    public WeeklyAiService(ObjectMapper objectMapper,
                           @Value("${openai.api-key}") String openaiApiKey) {
        this.objectMapper = objectMapper;
        this.openaiApiKey = openaiApiKey;
    }

    public String generateOrganizer(GetWeeklyResponse weeklyData) {

        final var tasksJson = toJson(weeklyData);

        final var systemPrompt = """
                You are an expert academic planner and well-being coach for university students.
                You receive a student's weekly tasks across courses and must create a balanced, \
                smart weekly organizer.

                Each course includes an "expectedGrade" field — this is the student's target grade \
                (e.g. "A+", "A", "B+"). Use this to calibrate effort and generate additional tasks.

                Your response MUST be valid JSON matching this exact structure:
                {
                  "weeklySummary": "Brief overview of the week's workload and strategy",
                  "dailyPlans": [
                    {
                      "day": "Monday",
                      "timeBlocks": [
                        {
                          "startTime": "09:00",
                          "endTime": "10:30",
                          "taskTitle": "task name",
                          "courseCode": "CS101",
                          "courseName": "Intro to CS",
                          "activityType": "DEEP_WORK | LIGHT_REVIEW | PRACTICE | READING | BREAK | ACTIVE_REST",
                          "effortLevel": "HIGH_FOCUS | MODERATE | LOW_EFFORT | REST",
                          "focusTip": "specific tip for this block",
                          "estimatedMinutes": 90
                        }
                      ],
                      "dailyTip": "tip for the day"
                    }
                  ],
                  "additionalTasks": [
                    {
                      "courseCode": "CS101",
                      "courseName": "Intro to CS",
                      "expectedGrade": "A+",
                      "taskTitle": "Extra practice: linked list problems",
                      "description": "Solve 5 medium-level linked list problems from LeetCode",
                      "reason": "Targeting A+ requires mastery beyond assignments",
                      "activityType": "DEEP_WORK | PRACTICE | READING | LIGHT_REVIEW",
                      "effortLevel": "HIGH_FOCUS | MODERATE | LOW_EFFORT",
                      "estimatedMinutes": 60
                    }
                  ],
                  "wellBeing": {
                    "activeRestSuggestions": ["15-min walk between study blocks", ...],
                    "breakStrategies": ["Pomodoro 25/5 for heavy tasks", ...],
                    "nutritionTips": ["Stay hydrated during focus blocks", ...],
                    "sleepRecommendations": ["Wind down by 22:00 on heavy days", ...],
                    "mindfulnessTips": ["2-min breathing before exams", ...],
                    "overallAdvice": "General balanced approach advice for the week"
                  }
                }

                Guidelines for scheduling:
                - Spread heavy tasks across different days, never stack all hard work on one day
                - Place HIGH_FOCUS tasks in the morning (09:00-12:00) when cognitive energy peaks
                - Schedule ACTIVE_REST blocks (walks, stretching) between intense sessions
                - Include BREAK blocks every 90 minutes of focused work
                - Consider due dates — prioritize tasks due sooner
                - Mix different subjects to avoid fatigue from one topic
                - Keep evenings lighter — review and light reading only
                - Plan weekends with recovery time but include light prep for next week
                - Be realistic: a student can do 6-8 productive hours per day max
                - Include estimated_hours from task data if available for time allocation

                Guidelines for additionalTasks based on expectedGrade:
                - A+ / A: generate extra deep-practice tasks, advanced readings, self-tests, \
                and concept summaries to push beyond the syllabus
                - A- / B+: suggest review sessions, practice problems, and light extra reading
                - B / B-: focus on solidifying core material — re-read notes, redo examples
                - C+ and below: suggest revisiting fundamentals, office hours, tutoring sessions
                - Always explain WHY the additional task helps reach the target grade in the "reason" field
                - Additional tasks should be integrated into the dailyPlans time blocks
                - Keep additional workload realistic — don't overwhelm, balance with well-being

                - Respond ONLY with the JSON, no markdown, no explanation
                """;

        final var userMessage = "Here is the student's weekly task data:\n" + tasksJson;


        final var client = OpenAIOkHttpClient.builder()
                .apiKey(openaiApiKey)
                .build();

        final var completion = client.chat().completions().create(
                ChatCompletionCreateParams.builder()
                        .model("gpt-5.4")
                        .maxCompletionTokens(4096)
                        .addMessage(ChatCompletionSystemMessageParam.builder()
                                .content(systemPrompt)
                                .build())
                        .addMessage(ChatCompletionUserMessageParam.builder()
                                .content(userMessage)
                                .build())
                        .build()
        );

        return completion.choices().get(0).message().content().orElse("");
    }

    private String toJson(GetWeeklyResponse data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize weekly data", e);
        }
    }
}