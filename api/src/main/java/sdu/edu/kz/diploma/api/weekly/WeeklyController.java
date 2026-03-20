package sdu.edu.kz.diploma.api.weekly;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdu.edu.kz.diploma.api.weekly.get.GetWeeklyResponse;

@RestController
@RequestMapping("/api/v1/weekly")
@RequiredArgsConstructor
public class WeeklyController {

    private final WeeklyDelegate weeklyDelegate;

    @GetMapping("/students/{studentId}/weeks/{weekNumber}")
    public ResponseEntity<GetWeeklyResponse> getByStudentAndWeek(@PathVariable Long studentId,
                                                                  @PathVariable Integer weekNumber) {
        return ResponseEntity.ok(weeklyDelegate.findByStudentAndWeek(studentId, weekNumber));
    }
}