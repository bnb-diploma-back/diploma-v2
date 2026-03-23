package sdu.edu.kz.diploma.api.dictionary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sdu.edu.kz.diploma.api.dictionary.create.CreateDepartmentRequest;
import sdu.edu.kz.diploma.api.dictionary.create.CreateDictionaryApi;
import sdu.edu.kz.diploma.api.dictionary.create.CreateMajorRequest;
import sdu.edu.kz.diploma.api.dictionary.delete.DeleteDictionaryApi;
import sdu.edu.kz.diploma.api.dictionary.get.GetDictionaryApi;
import sdu.edu.kz.diploma.api.dictionary.get.GetDictionaryResponse;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DictionaryDelegate {

    private final GetDictionaryApi getDictionaryApi;
    private final CreateDictionaryApi createDictionaryApi;
    private final DeleteDictionaryApi deleteDictionaryApi;

    public List<GetDictionaryResponse.DepartmentResponse> findAllDepartments() {
        return getDictionaryApi.findAllDepartments();
    }

    public GetDictionaryResponse.DepartmentResponse findDepartmentById(Long id) {
        return getDictionaryApi.findDepartmentById(id);
    }

    public List<GetDictionaryResponse.MajorResponse> findAllMajors() {
        return getDictionaryApi.findAllMajors();
    }

    public GetDictionaryResponse.MajorResponse findMajorById(Long id) {
        return getDictionaryApi.findMajorById(id);
    }

    public List<GetDictionaryResponse.MajorResponse> findMajorsByDepartmentId(Long departmentId) {
        return getDictionaryApi.findMajorsByDepartmentId(departmentId);
    }

    public Long createDepartment(CreateDepartmentRequest request) {
        return createDictionaryApi.createDepartment(request);
    }

    public Long createMajor(CreateMajorRequest request) {
        return createDictionaryApi.createMajor(request);
    }

    public void deleteDepartment(Long id) {
        deleteDictionaryApi.deleteDepartment(id);
    }

    public void deleteMajor(Long id) {
        deleteDictionaryApi.deleteMajor(id);
    }
}