package sdu.edu.kz.diploma.api.career.get;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCareerApi {

    private final GetCareerRepository repository;

    public GetCareerResponse findByStudentId(Long studentId) {
        return repository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
    }
}