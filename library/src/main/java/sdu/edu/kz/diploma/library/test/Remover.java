package sdu.edu.kz.diploma.library.test;

import org.springframework.stereotype.Component;

@Component
public class Remover extends AbstractEntityAction {

    public void all() {
        sessionTestRepository.deleteAll();
        userTestRepository.deleteAll();
        weeklyOrganizerTestRepository.deleteAll();
        studentTaskTestRepository.deleteAll();
        studentSyllabusTestRepository.deleteAll();
        studentCareerTestRepository.deleteAll();
        studentTestRepository.deleteAll();
        majorTestRepository.deleteAll();
        departmentTestRepository.deleteAll();
        weeklyPlanTestRepository.deleteAll();
        syllabusTestRepository.deleteAll();
    }

    public void syllabusById(Long id) {
        syllabusTestRepository.deleteById(id);
    }

    public void studentById(Long id) {
        studentTestRepository.deleteById(id);
    }

    public void studentTaskById(Long id) {
        studentTaskTestRepository.deleteById(id);
    }

    public void weeklyOrganizerById(Long id) {
        weeklyOrganizerTestRepository.deleteById(id);
    }
}