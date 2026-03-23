package sdu.edu.kz.diploma.api.dictionary.create;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDepartmentRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;
}