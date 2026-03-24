package sdu.edu.kz.diploma.library.model.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.entity.Session;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findByToken(String token);

    List<Session> findByUserIdAndRevokedFalse(Long userId);

    void deleteByUserId(Long userId);
}