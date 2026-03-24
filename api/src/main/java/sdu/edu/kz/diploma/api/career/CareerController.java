package sdu.edu.kz.diploma.api.career;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdu.edu.kz.diploma.api.auth.CurrentUser;
import sdu.edu.kz.diploma.api.career.generate.GenerateCareerResponse;
import sdu.edu.kz.diploma.api.career.get.GetCareerResponse;

@RestController
@RequestMapping("/api/v1/careers")
@RequiredArgsConstructor
@Tag(name = "Career Cards", description = "AI-powered career recommendations — analyzes student courses and grades to generate personalized profession cards with match percentages, gap analysis, and growth paths")
public class CareerController {

    private final CareerDelegate careerDelegate;

    @PostMapping("/students/{studentId}/generate")
    @Operation(
            summary = "Generate AI career cards",
            description = """
                    Analyzes the student's enrolled courses, expected grades, major, and department using OpenAI GPT-4o
                    to generate 3-5 ranked career path recommendations.

                    Each career card includes:
                    - **Profession** with industry domain and demand level (HIGH/MEDIUM/LOW/EMERGING)
                    - **Match percentage** based on courses and grades
                    - **Seniority path** (Junior -> Senior -> Staff growth trajectory)
                    - **Salary range** with global market data
                    - **Strength areas** — what courses already prepare them well
                    - **Gap areas** — honest gaps to fill
                    - **Recommended actions** — concrete steps (projects, certifications, courses)
                    - **Why this fits** — personalized 2-3 sentence explanation

                    Generated cards are saved to the database, replacing any existing career cards for this student.

                    Requires OPENAI_API_KEY environment variable.""",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Career cards generated and saved"),
                    @ApiResponse(responseCode = "500", description = "Student not found or AI service error")
            }
    )
    public ResponseEntity<GenerateCareerResponse> generate(
            @Parameter(description = "Student database ID", example = "1") @PathVariable Long studentId) {
        return ResponseEntity.ok(careerDelegate.generate(studentId));
    }

    @GetMapping("/students/{studentId}")
    @Operation(
            summary = "Get saved career cards",
            description = "Retrieves previously generated career cards from the database. Returns basic profession info (name, description, required skills). Call POST /generate first to create career cards.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Career cards retrieved"),
                    @ApiResponse(responseCode = "500", description = "Student not found")
            }
    )
    public ResponseEntity<GetCareerResponse> getByStudentId(
            @Parameter(description = "Student database ID", example = "1") @PathVariable Long studentId) {
        return ResponseEntity.ok(careerDelegate.findByStudentId(studentId));
    }

    @PostMapping("/me/generate")
    @Operation(
            summary = "Generate my AI career cards",
            description = "Same as POST /students/{id}/generate but uses the current user's student profile.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Career cards generated")
            }
    )
    public ResponseEntity<GenerateCareerResponse> generateMy() {
        return ResponseEntity.ok(careerDelegate.generate(CurrentUser.studentId()));
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get my saved career cards",
            description = "Same as GET /students/{id} but uses the current user's student profile.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Career cards retrieved")
            }
    )
    public ResponseEntity<GetCareerResponse> getMy() {
        return ResponseEntity.ok(careerDelegate.findByStudentId(CurrentUser.studentId()));
    }
}