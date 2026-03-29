package sdu.edu.kz.diploma.library.test.builder;

import sdu.edu.kz.diploma.library.model.enums.Role;
import sdu.edu.kz.diploma.library.model.entity.Student;
import sdu.edu.kz.diploma.library.model.entity.User;
import sdu.edu.kz.diploma.library.test.Randomizer;

public class UserTest {

    private static final Randomizer r = new Randomizer();

    private String email = r.email();
    private String password = "$2a$10$dummyHashedPasswordForTesting123456789012345678";
    private String firstName = r.name();
    private String lastName = r.name();
    private Role role = Role.STUDENT;
    private Student student;

    public UserTest email(String email) {
        this.email = email;
        return this;
    }

    public UserTest password(String password) {
        this.password = password;
        return this;
    }

    public UserTest firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserTest lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserTest role(Role role) {
        this.role = role;
        return this;
    }

    public UserTest student(Student student) {
        this.student = student;
        return this;
    }

    public User build() {
        return User.builder()
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .student(student)
                .build();
    }
}