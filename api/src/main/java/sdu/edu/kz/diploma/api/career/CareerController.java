package sdu.edu.kz.diploma.api.career;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdu.edu.kz.diploma.api.career.generate.GenerateCareerResponse;
import sdu.edu.kz.diploma.api.career.get.GetCareerResponse;

@RestController
@RequestMapping("/api/v1/careers")
@RequiredArgsConstructor
public class CareerController {

    private final CareerDelegate careerDelegate;

    @PostMapping("/students/{studentId}/generate")
    public ResponseEntity<GenerateCareerResponse> generate(@PathVariable Long studentId) {
        return ResponseEntity.ok(careerDelegate.generate(studentId));
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<GetCareerResponse> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(careerDelegate.findByStudentId(studentId));
    }
}