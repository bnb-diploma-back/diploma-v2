package sdu.edu.kz.diploma.api.syllabus.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateSyllabusApi {

    private final UpdateSyllabusRepository repository;

    @Transactional
    public void update(Long id, UpdateSyllabusRequest request) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Syllabus not found with id: " + id);
        }
        repository.updateSyllabus(id, request);
    }
}