package sdu.edu.kz.diploma.library.model.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.entity.WeeklyOrganizer;

import java.util.Optional;

public interface WeeklyOrganizerRepository extends CrudRepository<WeeklyOrganizer, Long> {

    Optional<WeeklyOrganizer> findByStudentIdAndWeekNumber(Long studentId, Integer weekNumber);
}