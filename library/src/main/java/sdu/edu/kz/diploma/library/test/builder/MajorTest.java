package sdu.edu.kz.diploma.library.test.builder;

import sdu.edu.kz.diploma.library.model.entity.Department;
import sdu.edu.kz.diploma.library.model.entity.Major;
import sdu.edu.kz.diploma.library.test.Randomizer;

public class MajorTest {

    private static final Randomizer r = new Randomizer();

    private String code = r.code();
    private String name = r.name();
    private String description = r.text();
    private Department department;

    public MajorTest code(String code) {
        this.code = code;
        return this;
    }

    public MajorTest name(String name) {
        this.name = name;
        return this;
    }

    public MajorTest description(String description) {
        this.description = description;
        return this;
    }

    public MajorTest department(Department department) {
        this.department = department;
        return this;
    }

    public Major build() {
        return Major.builder()
                .code(code)
                .name(name)
                .description(description)
                .department(department)
                .build();
    }
}