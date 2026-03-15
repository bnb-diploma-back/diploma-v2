package sdu.edu.kz.diploma.library.model.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.entity.StudentSyllabus;

public interface StudentSyllabusRepository extends CrudRepository<StudentSyllabus, Long> {
}