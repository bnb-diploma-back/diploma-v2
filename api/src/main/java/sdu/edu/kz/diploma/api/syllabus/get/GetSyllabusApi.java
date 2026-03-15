package sdu.edu.kz.diploma.api.syllabus.get;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetSyllabusApi {

    private final GetSyllabusRepository repository;

    public List<GetSyllabusResponse> findAll() {
        return repository.findAll();
    }

    public GetSyllabusResponse findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Syllabus not found with id: " + id));
    }

    public GetSyllabusResponse findByCourseCode(String courseCode) {
        return repository.findByCourseCode(courseCode)
                .orElseThrow(() -> new RuntimeException("Syllabus not found with course code: " + courseCode));
    }
}