package sdu.edu.kz.diploma.api.personalized;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    private Long studentId;
    private String message;
    private String sessionId;
}