package sdu.edu.kz.diploma.library.model.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.entity.Syllabus;
import sdu.edu.kz.diploma.library.model.enums.Semester;

import java.util.Optional;

public interface SyllabusRepository extends CrudRepository<Syllabus, Long> {

    Optional<Syllabus> findByCourseCodeAndSemester(String courseCode, Semester semester);
}