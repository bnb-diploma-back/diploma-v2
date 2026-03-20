package sdu.edu.kz.diploma.api.career.generate;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateCareerResponse {

    private Long studentId;
    private String studentName;
    private String major;
    private List<CareerCardResponse> careerCards;
    private LocalDateTime generatedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CareerCardResponse {
        private String profession;
        private String description;
        private String requiredSkills;
        private String industryDomain;
        private String seniorityPath;
        private String averageSalaryRange;
        private String demandLevel;
        private List<String> matchingCourses;
        private Integer matchPercentage;
        private List<String> strengthAreas;
        private List<String> gapAreas;
        private List<String> recommendedActions;
        private String whyThisFits;
    }
}