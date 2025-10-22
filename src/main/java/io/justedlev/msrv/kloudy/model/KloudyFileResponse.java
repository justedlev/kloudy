package io.justedlev.msrv.kloudy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.http.MediaType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Builder
@Schema(description = "Kloudy file information model")
public record KloudyFileResponse(
        @Schema(description = "Unique identifier of the file", accessMode = Schema.AccessMode.READ_ONLY)
        UUID id,
        @Schema(description = "ID of creator")
        String createdBy,
        @Schema(
                description = "Date and time of creation",
                format = "ISO 8601",
                example = "2024-10-18 23:48:43.344"
        )
        LocalDateTime createdAt,
        @Schema(description = "ID of modifier")
        String modifiedBy,
        @Schema(
                description = "Date and time of last modification",
                format = "ISO 8601",
                example = "2024-10-18 23:48:43.344"
        )
        LocalDateTime modifiedAt,
        @Schema(description = "Version of the file")
        Long version,
        @Schema(description = "Name of the file")
        String filename,
        @Schema(
                description = "MIME type",
                examples = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE}
        )
        String contentType,
        @Schema(description = "File length in bytes")
        Long length,
        @Schema(description = "File extension", examples = {"exe", "png", "mp3"})
        String extension,
        @Schema(description = "Attributes")
        Map<String, String> attributes
) implements FluentAttributable<String>, Serializable {
    @Serial
    private static final long serialVersionUID = 3707L;

    public Optional<String> attribute(String name) {
        return Optional.ofNullable(name).map(attributes::get);
    }
}
