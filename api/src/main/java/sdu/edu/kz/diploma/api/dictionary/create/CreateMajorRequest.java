package sdu.edu.kz.diploma.api.dictionary.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMajorRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Long departmentId;
}