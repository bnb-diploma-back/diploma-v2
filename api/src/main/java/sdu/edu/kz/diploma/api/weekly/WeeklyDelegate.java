package sdu.edu.kz.diploma.api.weekly;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sdu.edu.kz.diploma.api.weekly.get.GetWeeklyApi;
import sdu.edu.kz.diploma.api.weekly.get.GetWeeklyResponse;

@Component
@RequiredArgsConstructor
public class WeeklyDelegate {

    private final GetWeeklyApi getWeeklyApi;

    public GetWeeklyResponse findByStudentAndWeek(Long studentId, Integer weekNumber) {
        return getWeeklyApi.findByStudentAndWeek(studentId, weekNumber);
    }
}