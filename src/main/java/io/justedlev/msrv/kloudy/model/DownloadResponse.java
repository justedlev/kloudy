package io.justedlev.msrv.kloudy.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
public class DownloadResponse implements Serializable {
    private String filename;
    private String extension;
    private Instant modifiedAt;
    @lombok.Builder.Default
    private MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
    @lombok.Builder.Default
    private Long length = 0L;
    private transient Resource resource;
}
