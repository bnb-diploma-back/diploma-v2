package sdu.edu.kz.diploma.library.model.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.entity.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {
}