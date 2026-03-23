package sdu.edu.kz.diploma.api.weekly;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdu.edu.kz.diploma.api.weekly.get.GetWeeklyResponse;
import sdu.edu.kz.diploma.api.weekly.organize.OrganizeWeeklyResponse;

@RestController
@RequestMapping("/api/v1/weekly")
@RequiredArgsConstructor
@Tag(name = "Weekly Planner", description = "Weekly task overview and AI-powered study organizer — view tasks by week, generate balanced study plans with well-being advice")
public class WeeklyController {

    private final WeeklyDelegate weeklyDelegate;

    @GetMapping("/students/{studentId}/weeks/{weekNumber}")
    @Operation(
            summary = "Get weekly tasks by student and week",
            description = """
                    Returns all tasks for a given student in a specific week, grouped by course.
                    Each course includes: course code, title, instructor, credits, expected grade, and a list of tasks.
                    Tasks include: title, description, instructions, type (HOMEWORK/LAB/MIDTERM/etc.), status, due date, scores.""",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Weekly tasks retrieved"),
                    @ApiResponse(responseCode = "500", description = "Student not found")
            }
    )
    public ResponseEntity<GetWeeklyResponse> getByStudentAndWeek(
            @Parameter(description = "Student database ID", example = "1") @PathVariable Long studentId,
            @Parameter(description = "Academic week number (1-15)", example = "3") @PathVariable Integer weekNumber) {
        return ResponseEntity.ok(weeklyDelegate.findByStudentAndWeek(studentId, weekNumber));
    }

    @PostMapping("/students/{studentId}/weeks/{weekNumber}/organize")
    @Operation(
            summary = "Generate AI weekly organizer",
            description = """
                    Sends the student's weekly tasks to OpenAI GPT-4o and generates a balanced study plan.
                    The AI creates:
                    - **Daily plans** with time blocks (start/end time, effort level, focus tips)
                    - **Additional tasks** calibrated to target grades (A+ gets deep practice, C+ gets fundamentals)
                    - **Well-being advice** (active rest, break strategies, nutrition, sleep, mindfulness)

                    The generated plan is saved to the database and can be retrieved later via GET.
                    Calling POST again regenerates and replaces the existing plan.

                    Requires OPENAI_API_KEY environment variable.""",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Organizer generated and saved"),
                    @ApiResponse(responseCode = "500", description = "Student not found or AI service error")
            }
    )
    public ResponseEntity<OrganizeWeeklyResponse> organize(
            @Parameter(description = "Student database ID", example = "1") @PathVariable Long studentId,
            @Parameter(description = "Academic week number (1-15)", example = "3") @PathVariable Integer weekNumber) {
        return ResponseEntity.ok(weeklyDelegate.organize(studentId, weekNumber));
    }

    @GetMapping("/students/{studentId}/weeks/{weekNumber}/organize")
    @Operation(
            summary = "Get saved AI weekly organizer",
            description = """
                    Retrieves a previously generated AI study plan for the given student and week.
                    Returns the same structure as POST but from the database (no new AI call).
                    Call POST first to generate a plan before using this endpoint.""",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Saved organizer retrieved"),
                    @ApiResponse(responseCode = "500", description = "No organizer found — call POST first")
            }
    )
    public ResponseEntity<OrganizeWeeklyResponse> getOrganized(
            @Parameter(description = "Student database ID", example = "1") @PathVariable Long studentId,
            @Parameter(description = "Academic week number (1-15)", example = "3") @PathVariable Integer weekNumber) {
        return ResponseEntity.ok(weeklyDelegate.getOrganized(studentId, weekNumber));
    }
}