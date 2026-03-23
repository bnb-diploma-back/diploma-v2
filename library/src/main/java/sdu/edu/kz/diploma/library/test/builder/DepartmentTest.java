package sdu.edu.kz.diploma.library.test.builder;

import sdu.edu.kz.diploma.library.model.entity.Department;
import sdu.edu.kz.diploma.library.test.Randomizer;

public class DepartmentTest {

    private static final Randomizer r = new Randomizer();

    private String code = r.code();
    private String name = r.name();
    private String description = r.text();

    public DepartmentTest code(String code) {
        this.code = code;
        return this;
    }

    public DepartmentTest name(String name) {
        this.name = name;
        return this;
    }

    public DepartmentTest description(String description) {
        this.description = description;
        return this;
    }

    public Department build() {
        return Department.builder()
                .code(code)
                .name(name)
                .description(description)
                .build();
    }
}