package io.justedlev.msrv.kloudy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.http.MediaType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@SuperBuilder
@Schema(title = "Kloudy File Response", description = "Kloudy file model information")
public class KloudyFileResponse implements Serializable {

    @Schema(
            title = "ID",
            description = "Unique identifier of the file",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private UUID id;

    @Schema(
            title = "Created By",
            description = "ID of creator"
    )
    private String createdBy;

    @Schema(
            title = "Created At",
            description = "Date and time of creation",
            format = "ISO 8601",
            example = "2024-10-18 23:48:43.344"
    )
    private LocalDateTime createdAt;

    @Schema(
            title = "Modified By",
            description = "ID of modifier"
    )
    private String modifiedBy;

    @Schema(
            title = "Modified At",
            description = "Date and time of last modification",
            format = "ISO 8601",
            example = "2024-10-18 23:48:43.344"
    )
    private LocalDateTime modifiedAt;

    @Schema(
            title = "Version",
            description = "Version of the file"
    )
    private Long version;

    @Schema(
            title = "Filename",
            description = "Name of the file"
    )
    private String filename;

    @Schema(
            title = "Type",
            description = "MIME type",
            examples = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE}
    )
    private String type;

    @Schema(
            title = "Length",
            description = "File length in bytes"
    )
    private Long length;

    @Schema(
            title = "Extension",
            description = "File extension",
            examples = {"exe", "png", "mp3"}
    )
    private String extension;

}
