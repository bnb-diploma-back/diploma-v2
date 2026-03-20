package sdu.edu.kz.diploma.library.model.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.entity.StudentTask;

public interface StudentTaskRepository extends CrudRepository<StudentTask, Long> {
}