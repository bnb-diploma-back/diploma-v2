package sdu.edu.kz.diploma.api.weekly.get;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetWeeklyApi {

    private final GetWeeklyRepository repository;

    public GetWeeklyResponse findByStudentAndWeek(Long studentId, Integer weekNumber) {
        return repository.findByStudentAndWeek(studentId, weekNumber)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
    }
}