package sdu.edu.kz.diploma.api.student.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class DeleteStudentApi {

    private final StudentRepository studentRepository;

    @Transactional
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}