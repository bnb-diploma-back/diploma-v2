package sdu.edu.kz.diploma.library.test;

import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.library.test.repository.StudentCareerTestRepository;
import sdu.edu.kz.diploma.library.test.repository.StudentSyllabusTestRepository;
import sdu.edu.kz.diploma.library.test.repository.StudentTestRepository;
import sdu.edu.kz.diploma.library.test.repository.SyllabusTestRepository;
import sdu.edu.kz.diploma.library.test.repository.WeeklyPlanTestRepository;

public abstract class AbstractEntityAction {

    @Autowired
    protected SyllabusTestRepository syllabusTestRepository;

    @Autowired
    protected WeeklyPlanTestRepository weeklyPlanTestRepository;

    @Autowired
    protected StudentTestRepository studentTestRepository;

    @Autowired
    protected StudentSyllabusTestRepository studentSyllabusTestRepository;

    @Autowired
    protected StudentCareerTestRepository studentCareerTestRepository;
}