package sdu.edu.kz.diploma.api.syllabus.update;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sdu.edu.kz.diploma.library.syllabus.entity.Syllabus;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UpdateSyllabusRepository {

    private final EntityManager entityManager;

    public Optional<Syllabus> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Syllabus.class, id));
    }

    public Syllabus save(Syllabus syllabus) {
        return entityManager.merge(syllabus);
    }
}