package sdu.edu.kz.diploma.api.dictionary.get;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetDictionaryApi {

    private final GetDictionaryRepository repository;

    public List<GetDictionaryResponse.DepartmentResponse> findAllDepartments() {
        return repository.findAllDepartments();
    }

    public GetDictionaryResponse.DepartmentResponse findDepartmentById(Long id) {
        return repository.findDepartmentById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    public List<GetDictionaryResponse.MajorResponse> findAllMajors() {
        return repository.findAllMajors();
    }

    public GetDictionaryResponse.MajorResponse findMajorById(Long id) {
        return repository.findMajorById(id)
                .orElseThrow(() -> new RuntimeException("Major not found with id: " + id));
    }

    public List<GetDictionaryResponse.MajorResponse> findMajorsByDepartmentId(Long departmentId) {
        return repository.findMajorsByDepartmentId(departmentId);
    }
}