package sdu.edu.kz.diploma.library.test;

import org.springframework.beans.factory.annotation.Autowired;
import sdu.edu.kz.diploma.library.test.repository.*;

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

    @Autowired
    protected StudentTaskTestRepository studentTaskTestRepository;

    @Autowired
    protected WeeklyOrganizerTestRepository weeklyOrganizerTestRepository;

    @Autowired
    protected DepartmentTestRepository departmentTestRepository;

    @Autowired
    protected MajorTestRepository majorTestRepository;

    @Autowired
    protected UserTestRepository userTestRepository;

    @Autowired
    protected SessionTestRepository sessionTestRepository;

    @Autowired
    protected ChatHistoryTestRepository chatHistoryTestRepository;
}