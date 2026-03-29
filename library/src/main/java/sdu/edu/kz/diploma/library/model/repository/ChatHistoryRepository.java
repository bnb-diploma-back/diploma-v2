package sdu.edu.kz.diploma.library.model.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.entity.ChatHistory;

import java.util.List;

public interface ChatHistoryRepository extends CrudRepository<ChatHistory, Long> {

    List<ChatHistory> findByStudentIdOrderByCreatedAtAsc(Long studentId);
}