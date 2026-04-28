package sdu.edu.kz.diploma.api.syllabus.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.repository.SyllabusRepository;

@Service
@RequiredArgsConstructor
public class DeleteSyllabusApi {

    private final SyllabusRepository syllabusRepository;

    @Transactional
    public void delete(Long id) {
        if (!syllabusRepository.existsById(id)) {
            throw new sdu.edu.kz.diploma.api.exception.NotFoundException("Syllabus not found with id: " + id);
        }
        syllabusRepository.deleteById(id);
    }
}