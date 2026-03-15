package sdu.edu.kz.diploma.library.model.syllabus.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.syllabus.entity.WeeklyPlan;

public interface WeeklyPlanRepository extends CrudRepository<WeeklyPlan, Long> {
}