package sdu.edu.kz.diploma.api.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sdu.edu.kz.diploma.api.dashboard.get.GetDashboardApi;
import sdu.edu.kz.diploma.api.dashboard.get.GetDashboardResponse;

@Component
@RequiredArgsConstructor
public class DashboardDelegate {

    private final GetDashboardApi getDashboardApi;

    public GetDashboardResponse findByStudentId(Long studentId, Integer currentWeek) {
        return getDashboardApi.findByStudentId(studentId, currentWeek);
    }
}