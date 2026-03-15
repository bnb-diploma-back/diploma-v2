package sdu.edu.kz.diploma.api.student.get;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetStudentApi {

    private final GetStudentRepository repository;

    public List<GetStudentResponse> findAll() {
        return repository.findAll();
    }

    public GetStudentResponse findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public GetStudentResponse findByStudentId(String studentId) {
        return repository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with student id: " + studentId));
    }
}