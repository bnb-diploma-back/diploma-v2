package sdu.edu.kz.diploma.api.personalized.get;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetChatHistoryResponse {

    private Long id;
    private String role;
    private String content;
    private LocalDateTime createdAt;
}
