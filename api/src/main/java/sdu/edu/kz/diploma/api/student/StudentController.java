package sdu.edu.kz.diploma.api.student;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdu.edu.kz.diploma.api.student.create.CreateStudentRequest;
import sdu.edu.kz.diploma.api.student.get.GetStudentResponse;
import sdu.edu.kz.diploma.api.student.update.UpdateStudentRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Student profile management — CRUD operations for student records, enrollments, and career associations")
public class StudentController {

    private final StudentDelegate studentDelegate;

    @GetMapping
    @Operation(
            summary = "Get all students",
            description = "Returns a list of all registered students with their enrolled courses and career cards"
    )
    public ResponseEntity<List<GetStudentResponse>> getAll() {
        return ResponseEntity.ok(studentDelegate.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get student by ID",
            description = "Returns a single student profile including enrolled syllabi with expected grades and career cards",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Student found"),
                    @ApiResponse(responseCode = "500", description = "Student not found")
            }
    )
    public ResponseEntity<GetStudentResponse> getById(
            @Parameter(description = "Student database ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(studentDelegate.findById(id));
    }

    @GetMapping("/by-student-id/{studentId}")
    @Operation(
            summary = "Get student by student ID code",
            description = "Looks up a student by their university-issued student ID (e.g. SDU_ABC123)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Student found"),
                    @ApiResponse(responseCode = "500", description = "Student not found")
            }
    )
    public ResponseEntity<GetStudentResponse> getByStudentId(
            @Parameter(description = "University student ID code", example = "SDU_ABC123") @PathVariable String studentId) {
        return ResponseEntity.ok(studentDelegate.findByStudentId(studentId));
    }

    @PostMapping
    @Operation(
            summary = "Create a new student",
            description = "Registers a new student with optional course enrollments (syllabi with expected grades) and career associations. Returns the created student's database ID.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Student created successfully")
            }
    )
    public ResponseEntity<Long> create(@Valid @RequestBody CreateStudentRequest request) {
        final var id = studentDelegate.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing student",
            description = "Updates student profile fields, replaces enrolled syllabi and career associations",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Student updated"),
                    @ApiResponse(responseCode = "500", description = "Student not found")
            }
    )
    public ResponseEntity<Void> update(
            @Parameter(description = "Student database ID") @PathVariable Long id,
            @Valid @RequestBody UpdateStudentRequest request) {
        studentDelegate.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a student",
            description = "Permanently removes a student and all associated data (enrollments, careers, tasks)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Student deleted"),
                    @ApiResponse(responseCode = "500", description = "Student not found")
            }
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "Student database ID") @PathVariable Long id) {
        studentDelegate.delete(id);
        return ResponseEntity.noContent().build();
    }
}