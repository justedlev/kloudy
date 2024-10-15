package io.justedlev.kloudy.kloudy.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder(builderClassName = "Builder")
public class KloudyFileFilterParams implements Serializable {

    @Parameter(description = "Free text")
    @NotBlank
    private String q;

    @Parameter(description = "...")
    @Positive
    private Long length;

    @Parameter(description = "...")
    @NotBlank
    private String extension;

}
