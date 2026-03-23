package sdu.edu.kz.diploma.library.test;

import org.springframework.stereotype.Component;
import sdu.edu.kz.diploma.library.model.entity.*;
import sdu.edu.kz.diploma.library.test.builder.*;

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

    public StudentTask studentTask(Student student, Syllabus syllabus) {
        return studentTaskTestRepository.save(
                new StudentTaskTest().student(student).syllabus(syllabus).build()
        );
    }

    public StudentTask studentTask(Student student, Syllabus syllabus, int weekNumber) {
        return studentTaskTestRepository.save(
                new StudentTaskTest().student(student).syllabus(syllabus).weekNumber(weekNumber).build()
        );
    }

    public StudentTask studentTask(StudentTaskTest builder) {
        return studentTaskTestRepository.save(builder.build());
    }

    public WeeklyOrganizer weeklyOrganizer(Student student, int weekNumber) {
        return weeklyOrganizerTestRepository.save(
                new WeeklyOrganizerTest().student(student).weekNumber(weekNumber).build()
        );
    }

    public WeeklyOrganizer weeklyOrganizer(Student student, int weekNumber, String aiResponse) {
        return weeklyOrganizerTestRepository.save(
                new WeeklyOrganizerTest().student(student).weekNumber(weekNumber).aiResponse(aiResponse).build()
        );
    }

    public WeeklyOrganizer weeklyOrganizer(WeeklyOrganizerTest builder) {
        return weeklyOrganizerTestRepository.save(builder.build());
    }

    public Department department() {
        return departmentTestRepository.save(new DepartmentTest().build());
    }

    public Department department(String name) {
        return departmentTestRepository.save(new DepartmentTest().name(name).build());
    }

    public Department department(DepartmentTest builder) {
        return departmentTestRepository.save(builder.build());
    }

    public Major major(Department department) {
        return majorTestRepository.save(new MajorTest().department(department).build());
    }

    public Major major(Department department, String name) {
        return majorTestRepository.save(new MajorTest().department(department).name(name).build());
    }

    public Major major(MajorTest builder) {
        return majorTestRepository.save(builder.build());
    }
}