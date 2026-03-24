package sdu.edu.kz.diploma.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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

                                **Authentication:** JWT Bearer token. Register/login via `/api/v1/auth`, \
                                then include `Authorization: Bearer <token>` header.

                                **Roles:**
                                - `STUDENT` — access to dashboard, weekly planner, careers, chat, profile
                                - `ADMIN` — full access + manage syllabi, dictionary, register new admins

                                **AI Integration:** Uses OpenAI GPT-4o. Requires `OPENAI_API_KEY` environment variable.

                                **WebSocket Chat:** Connect to `ws://localhost:%d/ws/chat` using SockJS + STOMP.
                                Send messages to `/app/chat`, subscribe to `/user/queue/chat` for streamed responses."""
                                .formatted(serverPort))
                        .contact(new Contact()
                                .name("SDU Diploma Project")
                                .email("info@sdu.edu.kz")))
                .components(new Components()
                        .addSecuritySchemes("Bearer", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token from /api/v1/auth/login")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local development server")))
                .tags(List.of(
                        new Tag().name("Authentication").description("Registration and login with JWT"),
                        new Tag().name("Dashboard").description("Student dashboard — single endpoint aggregating all key data"),
                        new Tag().name("Students").description("Student profile management"),
                        new Tag().name("Syllabi").description("Course syllabus management (admin write)"),
                        new Tag().name("Weekly Planner").description("Weekly tasks and AI study organizer"),
                        new Tag().name("Career Cards").description("AI-powered career recommendations"),
                        new Tag().name("Dictionary").description("Departments and majors (admin write)")
                ));
    }
}