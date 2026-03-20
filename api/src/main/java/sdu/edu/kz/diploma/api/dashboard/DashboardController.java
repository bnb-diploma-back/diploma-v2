package sdu.edu.kz.diploma.api.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdu.edu.kz.diploma.api.dashboard.get.GetDashboardResponse;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardDelegate dashboardDelegate;

    @GetMapping("/students/{studentId}")
    public ResponseEntity<GetDashboardResponse> getByStudentId(@PathVariable Long studentId,
                                                                @RequestParam(defaultValue = "1") Integer week) {
        return ResponseEntity.ok(dashboardDelegate.findByStudentId(studentId, week));
    }
}