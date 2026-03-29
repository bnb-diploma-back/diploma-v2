package sdu.edu.kz.diploma.library.test.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.entity.ChatHistory;

public interface ChatHistoryTestRepository extends CrudRepository<ChatHistory, Long> {
}