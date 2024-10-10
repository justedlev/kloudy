package io.justedlev.kloudy.kloudy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DownloadResponse {
    private static final Set<String> INLINE_MEDIA_TYPES = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    static {
        INLINE_MEDIA_TYPES.add("video");
        INLINE_MEDIA_TYPES.add("image");
        INLINE_MEDIA_TYPES.add("audio");
    }

    private String filename;
    private String extension;
    @Builder.Default
    private MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
    @Builder.Default
    private Long length = 0L;
    private Resource resource;
    @Builder.Default
    private HttpHeaders headers = new HttpHeaders();

    public ResponseEntity<Resource> toResponseEntity() {
        getHeaders().add(HttpHeaders.CONTENT_DISPOSITION, getContentDisposition());

        return ResponseEntity.ok()
                .headers(getHeaders())
                .contentLength(getLength())
                .contentType(getContentType())
                .body(getResource());
    }

    private String getContentDisposition() {
        return ContentDisposition.builder(getType())
                .filename(getFilename())
                .build()
                .toString();
    }

    private String getType() {
        return Optional.ofNullable(getContentType())
                .map(MediaType::getType)
                .filter(INLINE_MEDIA_TYPES::contains)
                .map(c -> "inline")
                .orElse("attachment");
    }
}
