package sdu.edu.kz.diploma.api.career;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sdu.edu.kz.diploma.api.career.generate.GenerateCareerApi;
import sdu.edu.kz.diploma.api.career.generate.GenerateCareerResponse;
import sdu.edu.kz.diploma.api.career.get.GetCareerApi;
import sdu.edu.kz.diploma.api.career.get.GetCareerResponse;

@Component
@RequiredArgsConstructor
public class CareerDelegate {

    private final GenerateCareerApi generateCareerApi;
    private final GetCareerApi getCareerApi;

    public GenerateCareerResponse generate(Long studentId) {
        return generateCareerApi.generate(studentId);
    }

    public GetCareerResponse findByStudentId(Long studentId) {
        return getCareerApi.findByStudentId(studentId);
    }
}