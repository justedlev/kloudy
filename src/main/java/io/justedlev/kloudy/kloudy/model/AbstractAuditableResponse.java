package io.justedlev.kloudy.kloudy.model;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Accessors(chain = true)
public abstract class AbstractAuditableResponse implements Serializable {

    @Parameter(description = "Created by")
    private String createdBy;

    @Parameter(description = "Created at")
    private LocalDateTime createdAt;

    @Parameter(description = "Last modified by")
    private String modifiedBy;

    @Parameter(description = "Last modified at")
    private LocalDateTime modifiedAt;

}
