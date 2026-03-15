package sdu.edu.kz.diploma.library.test;

import org.springframework.stereotype.Component;
import sdu.edu.kz.diploma.library.model.entity.Syllabus;
import sdu.edu.kz.diploma.library.test.builder.SyllabusTest;

@Component
public class Creator extends AbstractEntityAction {

    public Syllabus syllabus() {
        return syllabusTestRepository.save(new SyllabusTest().build());
    }

    public Syllabus syllabusWithWeeklyPlans(int weekCount) {
        return syllabusTestRepository.save(new SyllabusTest().withWeeklyPlans(weekCount).build());
    }

    public Syllabus fullSyllabus() {
        return syllabusTestRepository.save(new SyllabusTest().withFullWeeklyPlans().build());
    }

    public Syllabus syllabus(SyllabusTest builder) {
        return syllabusTestRepository.save(builder.build());
    }
}