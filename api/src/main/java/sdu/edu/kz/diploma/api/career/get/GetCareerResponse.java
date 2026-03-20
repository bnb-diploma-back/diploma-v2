package sdu.edu.kz.diploma.api.career.get;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCareerResponse {

    private Long studentId;
    private String studentName;
    private List<CareerCardResponse> careerCards;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CareerCardResponse {
        private Long id;
        private String profession;
        private String description;
        private String requiredSkills;
    }
}