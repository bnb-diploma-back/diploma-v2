package sdu.edu.kz.diploma.library.test;

import org.springframework.stereotype.Component;

@Component
public class Remover extends AbstractEntityAction {

    public void all() {
        weeklyPlanTestRepository.deleteAll();
        syllabusTestRepository.deleteAll();
    }

    public void syllabusById(Long id) {
        syllabusTestRepository.deleteById(id);
    }
}