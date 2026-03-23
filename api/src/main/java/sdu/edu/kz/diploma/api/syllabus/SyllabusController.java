package sdu.edu.kz.diploma.api.syllabus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdu.edu.kz.diploma.api.syllabus.create.CreateSyllabusRequest;
import sdu.edu.kz.diploma.api.syllabus.get.GetSyllabusResponse;
import sdu.edu.kz.diploma.api.syllabus.update.UpdateSyllabusRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/syllabi")
@RequiredArgsConstructor
@Tag(name = "Syllabi", description = "Course syllabus management — create and manage course content, weekly plans, learning outcomes, and assessment criteria")
public class SyllabusController {

    private final SyllabusDelegate syllabusDelegate;

    @GetMapping
    @Operation(
            summary = "Get all syllabi",
            description = "Returns all course syllabi with their weekly plans, learning outcomes, and assessment criteria"
    )
    public ResponseEntity<List<GetSyllabusResponse>> getAll() {
        return ResponseEntity.ok(syllabusDelegate.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get syllabus by ID",
            description = "Returns a single syllabus with full course details including weekly plans ordered by week number",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Syllabus found"),
                    @ApiResponse(responseCode = "500", description = "Syllabus not found")
            }
    )
    public ResponseEntity<GetSyllabusResponse> getById(
            @Parameter(description = "Syllabus database ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(syllabusDelegate.findById(id));
    }

    @GetMapping("/by-course-code/{courseCode}")
    @Operation(
            summary = "Get syllabus by course code",
            description = "Looks up a syllabus by its unique course code (e.g. CS201, MATH101)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Syllabus found"),
                    @ApiResponse(responseCode = "500", description = "Syllabus not found")
            }
    )
    public ResponseEntity<GetSyllabusResponse> getByCourseCode(
            @Parameter(description = "Course code", example = "CS201") @PathVariable String courseCode) {
        return ResponseEntity.ok(syllabusDelegate.findByCourseCode(courseCode));
    }

    @PostMapping
    @Operation(
            summary = "Create a new syllabus",
            description = "Creates a course syllabus with optional weekly plans. Each weekly plan includes topics, learning objectives, lecture/practice content, assignments, and readings.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Syllabus created successfully")
            }
    )
    public ResponseEntity<Long> create(@Valid @RequestBody CreateSyllabusRequest request) {
        final var id = syllabusDelegate.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing syllabus",
            description = "Updates syllabus fields and replaces all weekly plans with the new set provided",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Syllabus updated"),
                    @ApiResponse(responseCode = "500", description = "Syllabus not found")
            }
    )
    public ResponseEntity<Void> update(
            @Parameter(description = "Syllabus database ID") @PathVariable Long id,
            @Valid @RequestBody UpdateSyllabusRequest request) {
        syllabusDelegate.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a syllabus",
            description = "Permanently removes a syllabus and all associated weekly plans",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Syllabus deleted"),
                    @ApiResponse(responseCode = "500", description = "Syllabus not found")
            }
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "Syllabus database ID") @PathVariable Long id) {
        syllabusDelegate.delete(id);
        return ResponseEntity.noContent().build();
    }
}