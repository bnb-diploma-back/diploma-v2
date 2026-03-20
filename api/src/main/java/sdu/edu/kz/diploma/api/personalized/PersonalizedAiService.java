package sdu.edu.kz.diploma.api.personalized;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionSystemMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sdu.edu.kz.diploma.library.jooq.tables.pojos.StudentSyllabi;

import java.util.stream.Collectors;

import static sdu.edu.kz.diploma.library.jooq.Tables.*;

@Service
@RequiredArgsConstructor
public class PersonalizedAiService {

    private final DSLContext dsl;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    public void chat(Long studentId, String userMessage, String sessionId) {
        try {
            final var studentContext = buildStudentContext(studentId);
            final var client = OpenAIOkHttpClient.fromEnv();

            final var systemPrompt = """
                    You are a personalized AI academic assistant for a university student.
                    You know the student's profile, courses, grades, and career goals.
                    Use this context to give tailored, actionable advice.

                    Be friendly, supportive, and concise. Use the student's name when appropriate.
                    If asked about study strategies, consider their specific courses and target grades.
                    If asked about careers, reference their actual course strengths.
                    If asked about well-being, balance it with their academic load.

                    Student context:
                    """ + studentContext;

            final var stream = client.chat().completions().createStreaming(
                    ChatCompletionCreateParams.builder()
                            .model("gpt-4o")
                            .maxCompletionTokens(2048)
                            .addMessage(ChatCompletionSystemMessageParam.builder()
                                    .content(systemPrompt)
                                    .build())
                            .addMessage(ChatCompletionUserMessageParam.builder()
                                    .content(userMessage)
                                    .build())
                            .build()
            );

            stream.stream()
                    .flatMap(chunk -> chunk.choices().stream())
                    .forEach(choice -> {
                        final var delta = choice.delta();
                        if (delta != null) {
                            delta.content().ifPresent(text -> {
                                if (!text.isEmpty()) {
                                    messagingTemplate.convertAndSendToUser(
                                            sessionId,
                                            "/queue/chat",
                                            ChatResponse.chunk(text)
                                    );
                                }
                            });
                        }
                    });

            stream.close();

            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    "/queue/chat",
                    ChatResponse.done()
            );

        } catch (Exception e) {
            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    "/queue/chat",
                    ChatResponse.error("Failed to process message: " + e.getMessage())
            );
        }
    }

    private String buildStudentContext(Long studentId) {
        final var student = dsl.selectFrom(STUDENTS)
                .where(STUDENTS.ID.eq(studentId))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Students.class);

        if (student == null) {
            return "Student not found.";
        }

        final var syllabi = dsl.selectFrom(STUDENT_SYLLABI)
                .where(STUDENT_SYLLABI.STUDENT_ID.eq(studentId))
                .fetchInto(StudentSyllabi.class);

        final var coursesInfo = syllabi.stream()
                .map(ss -> {
                    final var syllabus = dsl.selectFrom(SYLLABI)
                            .where(SYLLABI.ID.eq(ss.syllabusId()))
                            .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Syllabi.class);
                    return syllabus.courseCode() + " - " + syllabus.title()
                            + " (target: " + ss.expectedGrade() + ", credits: " + syllabus.credits() + ")";
                })
                .collect(Collectors.joining("\n"));

        final var careers = dsl.selectFrom(STUDENT_CAREERS)
                .where(STUDENT_CAREERS.STUDENT_ID.eq(studentId))
                .fetchInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.StudentCareers.class);

        final var careersInfo = careers.stream()
                .map(c -> c.profession() + " — " + c.description())
                .collect(Collectors.joining("\n"));

        return """
                Name: %s %s
                Department: %s
                Major: %s
                Semester: %s
                Enrollment year: %s

                Courses:
                %s

                Career interests:
                %s
                """.formatted(
                student.firstName(), student.lastName(),
                student.department(), student.major(),
                student.currentSemester(), student.enrollmentYear(),
                coursesInfo.isEmpty() ? "No courses enrolled" : coursesInfo,
                careersInfo.isEmpty() ? "No career cards generated yet" : careersInfo
        );
    }
}