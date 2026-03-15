package sdu.edu.kz.diploma.api.syllabus.delete;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sdu.edu.kz.diploma.library.syllabus.entity.Syllabus;

@Repository
@RequiredArgsConstructor
public class DeleteSyllabusRepository {

    private final EntityManager entityManager;

    public Syllabus findById(Long id) {
        return entityManager.find(Syllabus.class, id);
    }

    public void delete(Syllabus syllabus) {
        entityManager.remove(syllabus);
    }
}