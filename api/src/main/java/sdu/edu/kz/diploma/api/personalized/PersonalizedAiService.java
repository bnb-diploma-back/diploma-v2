package sdu.edu.kz.diploma.api.personalized;

import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionSystemMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;
import com.openai.models.chat.completions.ChatCompletionAssistantMessageParam;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sdu.edu.kz.diploma.library.jooq.tables.pojos.StudentSyllabi;
import sdu.edu.kz.diploma.library.model.entity.ChatHistory;
import sdu.edu.kz.diploma.library.model.repository.ChatHistoryRepository;
import sdu.edu.kz.diploma.library.model.repository.StudentRepository;

import java.util.stream.Collectors;

import static sdu.edu.kz.diploma.library.jooq.Tables.*;

@Service
public class PersonalizedAiService {

    private final DSLContext dsl;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatHistoryRepository chatHistoryRepository;
    private final StudentRepository studentRepository;
    private final String openaiApiKey;

    public PersonalizedAiService(DSLContext dsl,
                                 SimpMessagingTemplate messagingTemplate,
                                 ChatHistoryRepository chatHistoryRepository,
                                 StudentRepository studentRepository,
                                 @Value("${openai.api-key}") String openaiApiKey) {
        this.dsl = dsl;
        this.messagingTemplate = messagingTemplate;
        this.chatHistoryRepository = chatHistoryRepository;
        this.studentRepository = studentRepository;
        this.openaiApiKey = openaiApiKey;
    }

    @Async
    public void chat(Long studentId, String userMessage, String sessionId) {
        final var destination = "/topic/chat/" + sessionId;

        try {
            final var student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

            chatHistoryRepository.save(ChatHistory.builder()
                    .student(student)
                    .role("USER")
                    .content(userMessage)
                    .build());

            final var studentContext = buildStudentContext(studentId);
            final var client = OpenAIOkHttpClient.builder()
                    .apiKey(openaiApiKey)
                    .build();

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

            final var paramsBuilder = ChatCompletionCreateParams.builder()
                    .model("gpt-5.4")
                    .maxCompletionTokens(2048)
                    .addMessage(ChatCompletionSystemMessageParam.builder()
                            .content(systemPrompt)
                            .build());

            final var history = chatHistoryRepository.findByStudentIdOrderByCreatedAtAsc(studentId);
            final var recentHistory = history.size() > 20
                    ? history.subList(history.size() - 20, history.size())
                    : history;

            for (final var entry : recentHistory) {
                if ("USER".equals(entry.getRole())) {
                    paramsBuilder.addMessage(ChatCompletionUserMessageParam.builder()
                            .content(entry.getContent())
                            .build());
                } else {
                    paramsBuilder.addMessage(ChatCompletionAssistantMessageParam.builder()
                            .content(entry.getContent())
                            .build());
                }
            }

            final var stream = client.chat().completions().createStreaming(paramsBuilder.build());

            final var fullResponse = new StringBuilder();

            stream.stream()
                    .flatMap(chunk -> chunk.choices().stream())
                    .forEach(choice -> {
                        final var delta = choice.delta();
                        if (delta != null) {
                            delta.content().ifPresent(text -> {
                                if (!text.isEmpty()) {
                                    fullResponse.append(text);
                                    messagingTemplate.convertAndSend(destination, ChatResponse.chunk(text));
                                }
                            });
                        }
                    });

            stream.close();

            chatHistoryRepository.save(ChatHistory.builder()
                    .student(student)
                    .role("ASSISTANT")
                    .content(fullResponse.toString())
                    .build());

            messagingTemplate.convertAndSend(destination, ChatResponse.done());

        } catch (Exception e) {
            messagingTemplate.convertAndSend(destination,
                    ChatResponse.error("Failed to process message: " + e.getMessage()));
        }
    }

    private String resolveDepartmentName(Long departmentId) {
        if (departmentId == null) return "N/A";
        final var dept = dsl.selectFrom(DEPARTMENTS)
                .where(DEPARTMENTS.ID.eq(departmentId))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Departments.class);
        return dept != null ? dept.name() : "N/A";
    }

    private String resolveMajorName(Long majorId) {
        if (majorId == null) return "N/A";
        final var major = dsl.selectFrom(MAJORS)
                .where(MAJORS.ID.eq(majorId))
                .fetchOneInto(sdu.edu.kz.diploma.library.jooq.tables.pojos.Majors.class);
        return major != null ? major.name() : "N/A";
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
                    final var deptName = syllabus.departmentId() != null
                            ? resolveDepartmentName(syllabus.departmentId()) : "N/A";
                    final var majorName = syllabus.majorId() != null
                            ? resolveMajorName(syllabus.majorId()) : "N/A";
                    return syllabus.courseCode() + " - " + syllabus.title()
                            + " (department: " + deptName + ", major: " + majorName
                            + ", target: " + ss.expectedGrade() + ", credits: " + syllabus.credits() + ")";
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
                resolveDepartmentName(student.departmentId()),
                resolveMajorName(student.majorId()),
                student.currentSemester(), student.enrollmentYear(),
                coursesInfo.isEmpty() ? "No courses enrolled" : coursesInfo,
                careersInfo.isEmpty() ? "No career cards generated yet" : careersInfo
        );
    }
}