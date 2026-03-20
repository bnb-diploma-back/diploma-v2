package sdu.edu.kz.diploma.api.weekly;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sdu.edu.kz.diploma.api.weekly.get.GetWeeklyApi;
import sdu.edu.kz.diploma.api.weekly.get.GetWeeklyResponse;
import sdu.edu.kz.diploma.api.weekly.organize.OrganizeWeeklyApi;
import sdu.edu.kz.diploma.api.weekly.organize.OrganizeWeeklyResponse;

@Component
@RequiredArgsConstructor
public class WeeklyDelegate {

    private final GetWeeklyApi getWeeklyApi;
    private final OrganizeWeeklyApi organizeWeeklyApi;

    public GetWeeklyResponse findByStudentAndWeek(Long studentId, Integer weekNumber) {
        return getWeeklyApi.findByStudentAndWeek(studentId, weekNumber);
    }

    public OrganizeWeeklyResponse organize(Long studentId, Integer weekNumber) {
        return organizeWeeklyApi.organize(studentId, weekNumber);
    }

    public OrganizeWeeklyResponse getOrganized(Long studentId, Integer weekNumber) {
        return organizeWeeklyApi.getOrganized(studentId, weekNumber);
    }
}