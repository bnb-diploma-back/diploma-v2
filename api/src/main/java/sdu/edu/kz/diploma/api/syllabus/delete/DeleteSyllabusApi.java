package sdu.edu.kz.diploma.api.syllabus.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteSyllabusApi {

    private final DeleteSyllabusRepository repository;

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Syllabus not found with id: " + id);
        }
        repository.deleteById(id);
    }
}