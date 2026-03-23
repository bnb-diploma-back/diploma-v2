package sdu.edu.kz.diploma.api.dictionary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdu.edu.kz.diploma.api.dictionary.create.CreateDepartmentRequest;
import sdu.edu.kz.diploma.api.dictionary.create.CreateMajorRequest;
import sdu.edu.kz.diploma.api.dictionary.get.GetDictionaryResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dictionary")
@RequiredArgsConstructor
@Tag(name = "Dictionary", description = "Reference data — departments and majors used across the platform for dropdowns and filters")
public class DictionaryController {

    private final DictionaryDelegate dictionaryDelegate;

    @GetMapping("/departments")
    @Operation(
            summary = "Get all departments",
            description = "Returns all departments with their associated majors. Used for dropdowns in student registration and profile editing."
    )
    public ResponseEntity<List<GetDictionaryResponse.DepartmentResponse>> getAllDepartments() {
        return ResponseEntity.ok(dictionaryDelegate.findAllDepartments());
    }

    @GetMapping("/departments/{id}")
    @Operation(
            summary = "Get department by ID",
            description = "Returns a single department with all its majors",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Department found"),
                    @ApiResponse(responseCode = "500", description = "Department not found")
            }
    )
    public ResponseEntity<GetDictionaryResponse.DepartmentResponse> getDepartmentById(
            @Parameter(description = "Department ID", example = "1") @PathVariable("id") Long id) {
        return ResponseEntity.ok(dictionaryDelegate.findDepartmentById(id));
    }

    @PostMapping("/departments")
    @Operation(
            summary = "Create a department",
            description = "Creates a new department. Code must be unique (e.g. CS, MATH, PHYS).",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Department created")
            }
    )
    public ResponseEntity<Long> createDepartment(@Valid @RequestBody CreateDepartmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dictionaryDelegate.createDepartment(request));
    }

    @DeleteMapping("/departments/{id}")
    @Operation(
            summary = "Delete a department",
            description = "Deletes a department and all its majors (cascade)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Department deleted"),
                    @ApiResponse(responseCode = "500", description = "Department not found")
            }
    )
    public ResponseEntity<Void> deleteDepartment(
            @Parameter(description = "Department ID") @PathVariable("id") Long id) {
        dictionaryDelegate.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/majors")
    @Operation(
            summary = "Get all majors",
            description = "Returns all majors across all departments with their department info"
    )
    public ResponseEntity<List<GetDictionaryResponse.MajorResponse>> getAllMajors() {
        return ResponseEntity.ok(dictionaryDelegate.findAllMajors());
    }

    @GetMapping("/majors/{id}")
    @Operation(
            summary = "Get major by ID",
            description = "Returns a single major with its department info",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Major found"),
                    @ApiResponse(responseCode = "500", description = "Major not found")
            }
    )
    public ResponseEntity<GetDictionaryResponse.MajorResponse> getMajorById(
            @Parameter(description = "Major ID", example = "1") @PathVariable("id") Long id) {
        return ResponseEntity.ok(dictionaryDelegate.findMajorById(id));
    }

    @GetMapping("/departments/{departmentId}/majors")
    @Operation(
            summary = "Get majors by department",
            description = "Returns all majors belonging to a specific department. Useful for cascading dropdowns."
    )
    public ResponseEntity<List<GetDictionaryResponse.MajorResponse>> getMajorsByDepartment(
            @Parameter(description = "Department ID", example = "1") @PathVariable("departmentId") Long departmentId) {
        return ResponseEntity.ok(dictionaryDelegate.findMajorsByDepartmentId(departmentId));
    }

    @PostMapping("/majors")
    @Operation(
            summary = "Create a major",
            description = "Creates a new major under a department. Code must be unique (e.g. SE, DS, AI).",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Major created"),
                    @ApiResponse(responseCode = "500", description = "Department not found")
            }
    )
    public ResponseEntity<Long> createMajor(@Valid @RequestBody CreateMajorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dictionaryDelegate.createMajor(request));
    }

    @DeleteMapping("/majors/{id}")
    @Operation(
            summary = "Delete a major",
            description = "Deletes a major",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Major deleted"),
                    @ApiResponse(responseCode = "500", description = "Major not found")
            }
    )
    public ResponseEntity<Void> deleteMajor(
            @Parameter(description = "Major ID") @PathVariable("id") Long id) {
        dictionaryDelegate.deleteMajor(id);
        return ResponseEntity.noContent().build();
    }
}