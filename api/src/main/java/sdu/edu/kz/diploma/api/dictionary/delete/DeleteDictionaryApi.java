package sdu.edu.kz.diploma.api.dictionary.delete;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.repository.DepartmentRepository;
import sdu.edu.kz.diploma.library.model.repository.MajorRepository;

@Service
@RequiredArgsConstructor
public class DeleteDictionaryApi {

    private final DepartmentRepository departmentRepository;
    private final MajorRepository majorRepository;

    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }

    @Transactional
    public void deleteMajor(Long id) {
        if (!majorRepository.existsById(id)) {
            throw new RuntimeException("Major not found with id: " + id);
        }
        majorRepository.deleteById(id);
    }
}