package sdu.edu.kz.diploma.library.test.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.entity.StudentTask;

public interface StudentTaskTestRepository extends CrudRepository<StudentTask, Long> {
}