package sdu.edu.kz.diploma.api.student;

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
public class StudentController {

    private final StudentDelegate studentDelegate;

    @GetMapping
    public ResponseEntity<List<GetStudentResponse>> getAll() {
        return ResponseEntity.ok(studentDelegate.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetStudentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentDelegate.findById(id));
    }

    @GetMapping("/by-student-id/{studentId}")
    public ResponseEntity<GetStudentResponse> getByStudentId(@PathVariable String studentId) {
        return ResponseEntity.ok(studentDelegate.findByStudentId(studentId));
    }

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody CreateStudentRequest request) {
        final var id = studentDelegate.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody UpdateStudentRequest request) {
        studentDelegate.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentDelegate.delete(id);
        return ResponseEntity.noContent().build();
    }
}