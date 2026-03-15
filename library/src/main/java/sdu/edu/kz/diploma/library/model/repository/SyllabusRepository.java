package sdu.edu.kz.diploma.library.model.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.entity.Syllabus;

public interface SyllabusRepository extends CrudRepository<Syllabus, Long> {
}