package sdu.edu.kz.diploma.api.personalized;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PersonalizedChatController {

    private final PersonalizedAiService personalizedAiService;

    @MessageMapping("/chat")
    public void handleChat(@Payload ChatMessage chatMessage,
                           @Header("simpSessionId") String sessionId) {
        personalizedAiService.chat(chatMessage.getStudentId(), chatMessage.getMessage(), sessionId);
    }
}