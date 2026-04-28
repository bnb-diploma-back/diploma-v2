package sdu.edu.kz.diploma.api.dashboard.get;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetDashboardApi {

    private final GetDashboardRepository repository;

    public GetDashboardResponse findByStudentId(Long studentId, Integer currentWeek) {
        return repository.findByStudentId(studentId, currentWeek)
                .orElseThrow(() -> new sdu.edu.kz.diploma.api.exception.NotFoundException("Student not found with id: " + studentId));
    }
}