package sdu.edu.kz.diploma.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8070}")
    private int serverPort;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SDU Academic Planner API")
                        .version("1.0.0")
                        .description("""
                                AI-powered academic planning platform for SDU university students.

                                **Core features:**
                                - Student and syllabus management with full CRUD
                                - Weekly task tracking grouped by course with target grade awareness
                                - AI-generated weekly study organizers with balanced scheduling and well-being advice
                                - AI-powered career card generation based on courses, grades, and major
                                - Aggregated dashboard for frontend home screen
                                - Real-time personalized AI chat via WebSocket (STOMP over SockJS)

                                **AI Integration:** Uses OpenAI GPT-4o. Requires `OPENAI_API_KEY` environment variable.

                                **WebSocket Chat:** Connect to `ws://localhost:%d/ws/chat` using SockJS + STOMP.
                                Send messages to `/app/chat`, subscribe to `/user/queue/chat` for streamed responses."""
                                .formatted(serverPort))
                        .contact(new Contact()
                                .name("SDU Diploma Project")
                                .email("info@sdu.edu.kz")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local development server")))
                .tags(List.of(
                        new Tag().name("Dashboard").description("Student dashboard — single endpoint aggregating all key data"),
                        new Tag().name("Students").description("Student profile management"),
                        new Tag().name("Syllabi").description("Course syllabus management"),
                        new Tag().name("Weekly Planner").description("Weekly tasks and AI study organizer"),
                        new Tag().name("Career Cards").description("AI-powered career recommendations")
                ));
    }
}