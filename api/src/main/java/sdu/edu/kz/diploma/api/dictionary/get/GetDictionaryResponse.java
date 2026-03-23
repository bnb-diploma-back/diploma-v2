package sdu.edu.kz.diploma.api.dictionary.get;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetDictionaryResponse {

    private List<DepartmentResponse> departments;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DepartmentResponse {
        private Long id;
        private String code;
        private String name;
        private String description;
        private List<MajorResponse> majors;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MajorResponse {
        private Long id;
        private String code;
        private String name;
        private String description;
        private Long departmentId;
        private String departmentName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}