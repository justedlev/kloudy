package io.justedlev.msrv.kloudy.model;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KloudyFileFilterParams implements Serializable {
    @Parameter(description = "Free text")
    private String q;
}
