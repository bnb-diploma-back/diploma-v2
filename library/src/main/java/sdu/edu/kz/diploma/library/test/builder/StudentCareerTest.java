package sdu.edu.kz.diploma.library.test.builder;

import sdu.edu.kz.diploma.library.model.entity.Student;
import sdu.edu.kz.diploma.library.model.entity.StudentCareer;
import sdu.edu.kz.diploma.library.test.Randomizer;

public class StudentCareerTest {

    private static final Randomizer r = new Randomizer();

    private Student student;
    private String profession = r.name();
    private String description = r.text();
    private String requiredSkills = r.text();

    public StudentCareerTest student(Student student) {
        this.student = student;
        return this;
    }

    public StudentCareerTest profession(String profession) {
        this.profession = profession;
        return this;
    }

    public StudentCareerTest description(String description) {
        this.description = description;
        return this;
    }

    public StudentCareerTest requiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
        return this;
    }

    public StudentCareer build() {
        return StudentCareer.builder()
                .student(student)
                .profession(profession)
                .description(description)
                .requiredSkills(requiredSkills)
                .build();
    }
}