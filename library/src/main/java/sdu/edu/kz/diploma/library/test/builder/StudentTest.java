package sdu.edu.kz.diploma.library.test.builder;

import sdu.edu.kz.diploma.library.model.entity.Student;
import sdu.edu.kz.diploma.library.test.Randomizer;

import java.time.LocalDate;

public class StudentTest {

    private static final Randomizer r = new Randomizer();

    private String firstName = r.name();
    private String lastName = r.name();
    private String email = r.email();
    private String studentId = r.code();
    private String department = r.name();
    private String major = r.name();
    private int enrollmentYear = r.intBetween(2020, 2026);
    private LocalDate dateOfBirth = LocalDate.now().minusYears(r.intBetween(18, 25));
    private String phone = r.str(10);
    private String address = r.text();

    public StudentTest firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public StudentTest lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public StudentTest email(String email) {
        this.email = email;
        return this;
    }

    public StudentTest studentId(String studentId) {
        this.studentId = studentId;
        return this;
    }

    public StudentTest department(String department) {
        this.department = department;
        return this;
    }

    public StudentTest major(String major) {
        this.major = major;
        return this;
    }

    public StudentTest enrollmentYear(int enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
        return this;
    }

    public StudentTest dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public Student build() {
        return Student.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .studentId(studentId)
                .department(department)
                .major(major)
                .enrollmentYear(enrollmentYear)
                .dateOfBirth(dateOfBirth)
                .phone(phone)
                .address(address)
                .build();
    }
}