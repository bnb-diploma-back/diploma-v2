package sdu.edu.kz.diploma.library.test;

import org.springframework.stereotype.Component;

@Component
public class Remover extends AbstractEntityAction {

    public void all() {
        studentSyllabusTestRepository.deleteAll();
        studentCareerTestRepository.deleteAll();
        studentTestRepository.deleteAll();
        weeklyPlanTestRepository.deleteAll();
        syllabusTestRepository.deleteAll();
    }

    public void syllabusById(Long id) {
        syllabusTestRepository.deleteById(id);
    }

    public void studentById(Long id) {
        studentTestRepository.deleteById(id);
    }
}