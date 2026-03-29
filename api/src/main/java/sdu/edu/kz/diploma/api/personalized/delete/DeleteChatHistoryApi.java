package sdu.edu.kz.diploma.api.personalized.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.repository.ChatHistoryRepository;

@Service
@RequiredArgsConstructor
public class DeleteChatHistoryApi {

    private final ChatHistoryRepository chatHistoryRepository;

    @Transactional
    public void deleteByStudentId(Long studentId) {
        final var messages = chatHistoryRepository.findByStudentIdOrderByCreatedAtAsc(studentId);
        chatHistoryRepository.deleteAll(messages);
    }
}