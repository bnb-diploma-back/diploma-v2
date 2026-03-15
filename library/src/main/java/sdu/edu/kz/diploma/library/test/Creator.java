package sdu.edu.kz.diploma.library.test;

import org.springframework.stereotype.Component;
import sdu.edu.kz.diploma.library.model.entity.Student;
import sdu.edu.kz.diploma.library.model.entity.StudentCareer;
import sdu.edu.kz.diploma.library.model.entity.StudentSyllabus;
import sdu.edu.kz.diploma.library.model.entity.Syllabus;
import sdu.edu.kz.diploma.library.test.builder.StudentCareerTest;
import sdu.edu.kz.diploma.library.test.builder.StudentSyllabusTest;
import sdu.edu.kz.diploma.library.test.builder.StudentTest;
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

    public Student student() {
        return studentTestRepository.save(new StudentTest().build());
    }

    public Student student(StudentTest builder) {
        return studentTestRepository.save(builder.build());
    }

    public StudentSyllabus studentSyllabus(Student student, Syllabus syllabus) {
        return studentSyllabusTestRepository.save(
                new StudentSyllabusTest().student(student).syllabus(syllabus).build()
        );
    }

    public StudentSyllabus studentSyllabus(Student student, Syllabus syllabus, String expectedGrade) {
        return studentSyllabusTestRepository.save(
                new StudentSyllabusTest().student(student).syllabus(syllabus).expectedGrade(expectedGrade).build()
        );
    }

    public StudentCareer studentCareer(Student student) {
        return studentCareerTestRepository.save(
                new StudentCareerTest().student(student).build()
        );
    }

    public StudentCareer studentCareer(Student student, String profession) {
        return studentCareerTestRepository.save(
                new StudentCareerTest().student(student).profession(profession).build()
        );
    }
}