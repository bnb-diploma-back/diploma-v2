package sdu.edu.kz.diploma.api.personalized;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sdu.edu.kz.diploma.api.personalized.delete.DeleteChatHistoryApi;
import sdu.edu.kz.diploma.api.personalized.get.GetChatHistoryApi;
import sdu.edu.kz.diploma.api.personalized.get.GetChatHistoryResponse;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class PersonalizedChatController {

    private final PersonalizedAiService personalizedAiService;
    private final GetChatHistoryApi getChatHistoryApi;
    private final DeleteChatHistoryApi deleteChatHistoryApi;

    @MessageMapping("/chat")
    public void handleChat(@Payload ChatMessage chatMessage) {
        personalizedAiService.chat(chatMessage.getStudentId(), chatMessage.getMessage(), chatMessage.getSessionId());
    }

    @GetMapping("/history/{studentId}")
    @ResponseBody
    public ResponseEntity<List<GetChatHistoryResponse>> getHistory(@PathVariable Long studentId) {
        return ResponseEntity.ok(getChatHistoryApi.findByStudentId(studentId));
    }

    @DeleteMapping("/history/{studentId}")
    @ResponseBody
    public ResponseEntity<Void> clearHistory(@PathVariable Long studentId) {
        deleteChatHistoryApi.deleteByStudentId(studentId);
        return ResponseEntity.noContent().build();
    }
}