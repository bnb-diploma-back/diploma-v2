package sdu.edu.kz.diploma.api.dictionary.update;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDepartmentRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;
}