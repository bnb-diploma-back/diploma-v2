package sdu.edu.kz.diploma.library.test;

import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.library.test.repository.SyllabusTestRepository;
import sdu.edu.kz.diploma.library.test.repository.WeeklyPlanTestRepository;

public abstract class AbstractEntityAction {

    @Autowired
    protected SyllabusTestRepository syllabusTestRepository;

    @Autowired
    protected WeeklyPlanTestRepository weeklyPlanTestRepository;
}