package io.justedlev.msrv.kloudy.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder(builderClassName = "Builder")
@Schema(description = "Kloudy file response")
public class KloudyFileResponse extends AbstractVersionableResponse implements Serializable {

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
