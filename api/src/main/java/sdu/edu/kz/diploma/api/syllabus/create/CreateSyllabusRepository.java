package sdu.edu.kz.diploma.api.syllabus.create;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sdu.edu.kz.diploma.library.syllabus.entity.Syllabus;

@Repository
@RequiredArgsConstructor
public class CreateSyllabusRepository {

    private final EntityManager entityManager;

    public Syllabus save(Syllabus syllabus) {
        entityManager.persist(syllabus);
        return syllabus;
    }
}