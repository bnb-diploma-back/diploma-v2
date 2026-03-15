package sdu.edu.kz.diploma.library.test.builder;

import sdu.edu.kz.diploma.library.model.entity.Student;
import sdu.edu.kz.diploma.library.model.entity.StudentSyllabus;
import sdu.edu.kz.diploma.library.model.entity.Syllabus;
import sdu.edu.kz.diploma.library.test.Randomizer;

public class StudentSyllabusTest {

    private static final Randomizer r = new Randomizer();

    private Student student;
    private Syllabus syllabus;
    private String expectedGrade = r.oneOf("A+", "A", "A-", "B+", "B", "B-", "C+", "C", "D", "F");
    private String notes = r.text();

    public StudentSyllabusTest student(Student student) {
        this.student = student;
        return this;
    }

    public StudentSyllabusTest syllabus(Syllabus syllabus) {
        this.syllabus = syllabus;
        return this;
    }

    public StudentSyllabusTest expectedGrade(String expectedGrade) {
        this.expectedGrade = expectedGrade;
        return this;
    }

    public StudentSyllabusTest notes(String notes) {
        this.notes = notes;
        return this;
    }

    public StudentSyllabus build() {
        return StudentSyllabus.builder()
                .student(student)
                .syllabus(syllabus)
                .expectedGrade(expectedGrade)
                .notes(notes)
                .build();
    }
}