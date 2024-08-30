package dev.justedlev.kloudy.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(builderClassName = "Builder")
@Schema(description = "Kloudy file response")
public class KloudyFileResponse implements Serializable {
    @Parameter(description = "Unique identifier of the file")
    private UUID id;

    @Parameter(description = "Name of the file")
    private String filename;

    @Parameter(description = "MIME type")
    private String mimeType;

    @Parameter(description = "File length in bytes")
    private Long length;

    @Parameter(description = "File extension")
    private String extension;
}
