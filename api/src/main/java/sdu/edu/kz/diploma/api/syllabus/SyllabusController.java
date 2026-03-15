package sdu.edu.kz.diploma.api.syllabus;

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
public class SyllabusController {

    private final SyllabusDelegate syllabusDelegate;

    @GetMapping
    public ResponseEntity<List<GetSyllabusResponse>> getAll() {
        return ResponseEntity.ok(syllabusDelegate.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetSyllabusResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(syllabusDelegate.findById(id));
    }

    @GetMapping("/by-course-code/{courseCode}")
    public ResponseEntity<GetSyllabusResponse> getByCourseCode(@PathVariable String courseCode) {
        return ResponseEntity.ok(syllabusDelegate.findByCourseCode(courseCode));
    }

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody CreateSyllabusRequest request) {
        final var id = syllabusDelegate.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody UpdateSyllabusRequest request) {
        syllabusDelegate.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        syllabusDelegate.delete(id);
        return ResponseEntity.noContent().build();
    }
}