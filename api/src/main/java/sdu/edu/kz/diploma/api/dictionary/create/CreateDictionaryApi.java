package sdu.edu.kz.diploma.api.dictionary.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.edu.kz.diploma.library.model.entity.Department;
import sdu.edu.kz.diploma.library.model.entity.Major;
import sdu.edu.kz.diploma.library.model.repository.DepartmentRepository;
import sdu.edu.kz.diploma.library.model.repository.MajorRepository;

@Service
@RequiredArgsConstructor
public class CreateDictionaryApi {

    private final DepartmentRepository departmentRepository;
    private final MajorRepository majorRepository;

    @Transactional
    public Long createDepartment(CreateDepartmentRequest request) {
        final var department = Department.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return departmentRepository.save(department).getId();
    }

    @Transactional
    public Long createMajor(CreateMajorRequest request) {
        final var department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new sdu.edu.kz.diploma.api.exception.NotFoundException("Department not found with id: " + request.getDepartmentId()));

        final var major = Major.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .department(department)
                .build();

        return majorRepository.save(major).getId();
    }
}