package sdu.edu.kz.diploma.api.personalized.get;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.repository.ChatHistoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetChatHistoryApi {

    private final ChatHistoryRepository chatHistoryRepository;

    @Transactional(readOnly = true)
    public List<GetChatHistoryResponse> findByStudentId(Long studentId) {
        return chatHistoryRepository.findByStudentIdOrderByCreatedAtAsc(studentId)
                .stream()
                .map(h -> GetChatHistoryResponse.builder()
                        .id(h.getId())
                        .role(h.getRole())
                        .content(h.getContent())
                        .createdAt(h.getCreatedAt())
                        .build())
                .toList();
    }
}