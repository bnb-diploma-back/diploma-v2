package sdu.edu.kz.diploma.api.career.generate;

import tools.jackson.databind.ObjectMapper;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionSystemMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CareerAiService {

    private final ObjectMapper objectMapper;
    private final String openaiApiKey;

    public CareerAiService(ObjectMapper objectMapper,
                           @Value("${openai.api-key}") String openaiApiKey) {
        this.objectMapper = objectMapper;
        this.openaiApiKey = openaiApiKey;
    }

    public String generateCareerCards(CareerAiInput input) {
        final var client = OpenAIOkHttpClient.builder()
                .apiKey(openaiApiKey)
                .build();

        final var inputJson = toJson(input);

        final var systemPrompt = """
                You are an expert career counselor and industry analyst for university students.
                You analyze a student's courses, grades, major, and department to suggest \
                the best-fitting career paths as profession cards.

                Your response MUST be valid JSON matching this exact structure:
                {
                  "careerCards": [
                    {
                      "profession": "Backend Developer",
                      "description": "Designs and builds server-side applications, APIs, and databases...",
                      "requiredSkills": "Java, Spring Boot, SQL, REST APIs, Microservices, Docker",
                      "industryDomain": "Software Engineering / IT",
                      "seniorityPath": "Junior → Mid → Senior → Staff → Principal Engineer",
                      "averageSalaryRange": "$60,000 - $150,000",
                      "demandLevel": "HIGH | MEDIUM | LOW | EMERGING",
                      "matchingCourses": ["CS201 - Data Structures", "CS301 - Databases"],
                      "matchPercentage": 85,
                      "strengthAreas": ["Strong algorithmic thinking from CS201", ...],
                      "gapAreas": ["No DevOps/Cloud coursework yet", ...],
                      "recommendedActions": ["Learn Docker basics", "Build a REST API project", ...],
                      "whyThisFits": "Your combination of CS201 (A) and CS301 (A+) shows strong backend aptitude..."
                    }
                  ]
                }

                Guidelines:
                - Generate 3-5 career cards, ranked by match percentage (best fit first)
                - Use the student's courses, grades, and major to calculate realistic match percentages
                - Higher grades in relevant courses = stronger match for related careers
                - Consider the student's department and major for domain alignment
                - Industry domains should be specific (not just "IT" — say "FinTech", "HealthTech", etc.)
                - Salary ranges should reflect global/international market (mention currency)
                - Seniority path should show realistic 5-10 year growth trajectory
                - Strength areas: what courses/grades already prepare them well
                - Gap areas: what's missing — be honest but constructive
                - Recommended actions: concrete, actionable steps (courses, projects, certifications)
                - whyThisFits: personalized 2-3 sentence explanation connecting their profile to the career
                - demandLevel reflects current job market reality
                - Be diverse in suggestions — don't just suggest developer roles, consider \
                adjacent paths like product management, data analysis, consulting, research
                - Respond ONLY with the JSON, no markdown, no explanation
                """;

        final var userMessage = "Here is the student's academic profile:\n" + inputJson;

        final var completion = client.chat().completions().create(
                ChatCompletionCreateParams.builder()
                        .model("gpt-4o")
                        .maxCompletionTokens(4096)
                        .addMessage(ChatCompletionSystemMessageParam.builder()
                                .content(systemPrompt)
                                .build())
                        .addMessage(ChatCompletionUserMessageParam.builder()
                                .content(userMessage)
                                .build())
                        .build()
        );

        return completion.choices().getFirst().message().content().orElse("");
    }

    private String toJson(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize career input", e);
        }
    }
}