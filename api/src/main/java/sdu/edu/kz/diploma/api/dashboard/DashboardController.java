package sdu.edu.kz.diploma.api.dashboard;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdu.edu.kz.diploma.api.dashboard.get.GetDashboardResponse;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Student dashboard — single endpoint aggregating all key data for the frontend home screen")
public class DashboardController {

    private final DashboardDelegate dashboardDelegate;

    @GetMapping("/students/{studentId}")
    @Operation(
            summary = "Get student dashboard",
            description = """
                    Returns everything the frontend needs in one call:

                    - **Profile** — name, email, student ID, department, major, semester
                    - **Academic overview** — total courses, total credits, list of courses with expected grades
                    - **Current week** — task counts (total, completed, pending, overdue), whether an AI organizer exists
                    - **Upcoming deadlines** — top 10 nearest due tasks with days remaining, filtered to non-completed only
                    - **Task progress** — overall stats across all weeks: completion percentage, average score, status breakdown
                    - **Top careers** — up to 3 AI-generated career card professions
                    - **Today's schedule** — if an AI weekly organizer exists, extracts today's time blocks with effort levels and focus tips""",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dashboard data retrieved"),
                    @ApiResponse(responseCode = "500", description = "Student not found")
            }
    )
    public ResponseEntity<GetDashboardResponse> getByStudentId(
            @Parameter(description = "Student database ID", example = "1") @PathVariable Long studentId,
            @Parameter(description = "Current academic week number (1-15)", example = "3") @RequestParam(defaultValue = "1") Integer week) {
        return ResponseEntity.ok(dashboardDelegate.findByStudentId(studentId, week));
    }
}