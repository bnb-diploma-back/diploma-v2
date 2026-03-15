package sdu.edu.kz.diploma.library.model.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.entity.StudentCareer;

public interface StudentCareerRepository extends CrudRepository<StudentCareer, Long> {
}