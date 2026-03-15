package sdu.edu.kz.diploma.api.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sdu.edu.kz.diploma.api.student.create.CreateStudentApi;
import sdu.edu.kz.diploma.api.student.create.CreateStudentRequest;
import sdu.edu.kz.diploma.api.student.delete.DeleteStudentApi;
import sdu.edu.kz.diploma.api.student.get.GetStudentApi;
import sdu.edu.kz.diploma.api.student.get.GetStudentResponse;
import sdu.edu.kz.diploma.api.student.update.UpdateStudentApi;
import sdu.edu.kz.diploma.api.student.update.UpdateStudentRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StudentDelegate {

    private final GetStudentApi getStudentApi;
    private final CreateStudentApi createStudentApi;
    private final UpdateStudentApi updateStudentApi;
    private final DeleteStudentApi deleteStudentApi;

    public List<GetStudentResponse> findAll() {
        return getStudentApi.findAll();
    }

    public GetStudentResponse findById(Long id) {
        return getStudentApi.findById(id);
    }

    public GetStudentResponse findByStudentId(String studentId) {
        return getStudentApi.findByStudentId(studentId);
    }

    public Long create(CreateStudentRequest request) {
        return createStudentApi.create(request);
    }

    public void update(Long id, UpdateStudentRequest request) {
        updateStudentApi.update(id, request);
    }

    public void delete(Long id) {
        deleteStudentApi.delete(id);
    }
}