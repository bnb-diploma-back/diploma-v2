package sdu.edu.kz.diploma.library.model.repository;

import org.springframework.data.repository.CrudRepository;
import sdu.edu.kz.diploma.library.model.entity.WeeklyPlan;

public interface WeeklyPlanRepository extends CrudRepository<WeeklyPlan, Long> {
}