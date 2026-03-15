package sdu.edu.kz.diploma.library.model.syllabus.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.syllabus.entity.Syllabus;

public interface SyllabusRepository extends CrudRepository<Syllabus, Long> {
}