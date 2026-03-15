package sdu.edu.kz.diploma.library.test;

import org.springframework.stereotype.Component;
import sdu.edu.kz.diploma.library.model.entity.Semester;

import java.time.LocalDate;

@Component
public class Editor extends AbstractEntityAction {

    public void updateTitle(Long id, String title) {
        final var syllabus = syllabusTestRepository.findById(id).orElseThrow();
        syllabus.setTitle(title);
        syllabusTestRepository.save(syllabus);
    }

    public void updateCourseCode(Long id, String courseCode) {
        final var syllabus = syllabusTestRepository.findById(id).orElseThrow();
        syllabus.setCourseCode(courseCode);
        syllabusTestRepository.save(syllabus);
    }

    public void updateCredits(Long id, int credits) {
        final var syllabus = syllabusTestRepository.findById(id).orElseThrow();
        syllabus.setCredits(credits);
        syllabusTestRepository.save(syllabus);
    }

    public void updateInstructor(Long id, String instructor) {
        final var syllabus = syllabusTestRepository.findById(id).orElseThrow();
        syllabus.setInstructor(instructor);
        syllabusTestRepository.save(syllabus);
    }

    public void updateDepartment(Long id, String department) {
        final var syllabus = syllabusTestRepository.findById(id).orElseThrow();
        syllabus.setDepartment(department);
        syllabusTestRepository.save(syllabus);
    }

    public void updateSemester(Long id, Semester semester) {
        final var syllabus = syllabusTestRepository.findById(id).orElseThrow();
        syllabus.setSemester(semester);
        syllabusTestRepository.save(syllabus);
    }

    public void updateDates(Long id, LocalDate startDate, LocalDate endDate) {
        final var syllabus = syllabusTestRepository.findById(id).orElseThrow();
        syllabus.setStartDate(startDate);
        syllabus.setEndDate(endDate);
        syllabusTestRepository.save(syllabus);
    }
}