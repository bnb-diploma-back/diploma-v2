package sdu.edu.kz.diploma.library.model.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.entity.Department;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
}