package io.justedlev.msrv.kloudy.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@SuperBuilder
@Schema(description = "Kloudy file response")
public class KloudyFileResponse implements Serializable {

    @Parameter(description = "Unique identifier of the file")
    private UUID id;

    @Parameter(description = "ID of creator")
    private String createdBy;

    @Parameter(description = "Created at")
    private LocalDateTime createdAt;

    @Parameter(description = "ID of modifier")
    private String modifiedBy;

    @Parameter(description = "Last modified at")
    private LocalDateTime modifiedAt;

    @Parameter(description = "File version")
    private Long version;

    @Parameter(description = "Name of the file")
    private String filename;

    @Parameter(description = "MIME type")
    private String type;

    @Parameter(description = "File length in bytes")
    private Long length;

    @Parameter(description = "File extension")
    private String extension;

}
