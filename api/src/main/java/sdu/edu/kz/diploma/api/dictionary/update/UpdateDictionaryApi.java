package sdu.edu.kz.diploma.api.dictionary.update;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.repository.DepartmentRepository;
import sdu.edu.kz.diploma.library.model.repository.MajorRepository;

@Service
@RequiredArgsConstructor
public class UpdateDictionaryApi {

    private final DepartmentRepository departmentRepository;
    private final MajorRepository majorRepository;

    @Transactional
    public void updateDepartment(Long id, UpdateDepartmentRequest request) {
        final var department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        department.setCode(request.getCode());
        department.setName(request.getName());
        department.setDescription(request.getDescription());

        departmentRepository.save(department);
    }

    @Transactional
    public void updateMajor(Long id, UpdateMajorRequest request) {
        final var major = majorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Major not found with id: " + id));

        final var department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + request.getDepartmentId()));

        major.setCode(request.getCode());
        major.setName(request.getName());
        major.setDescription(request.getDescription());
        major.setDepartment(department);

        majorRepository.save(major);
    }
}