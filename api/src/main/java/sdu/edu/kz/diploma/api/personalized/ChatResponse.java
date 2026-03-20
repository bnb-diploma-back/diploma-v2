package sdu.edu.kz.diploma.api.personalized;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponse {

    private String content;
    private String type;
    private LocalDateTime timestamp;

    public static ChatResponse chunk(String content) {
        return ChatResponse.builder()
                .content(content)
                .type("CHUNK")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ChatResponse done() {
        return ChatResponse.builder()
                .content("")
                .type("DONE")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ChatResponse error(String message) {
        return ChatResponse.builder()
                .content(message)
                .type("ERROR")
                .timestamp(LocalDateTime.now())
                .build();
    }
}