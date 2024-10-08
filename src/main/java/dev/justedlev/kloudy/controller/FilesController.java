package dev.justedlev.kloudy.controller;

import dev.justedlev.kloudy.model.KloudyFileResponse;
import dev.justedlev.kloudy.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Tag(name = "Files v1", description = "Files API")
@RestController
@RequestMapping("/v1/files")
@RequiredArgsConstructor
@Validated
public class FilesController {
    private final AttachmentService attachmentService;

    @Operation(summary = "Upload file")
    @ApiResponse(responseCode = "201", description = "File successfully uploaded")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KloudyFileResponse> upload(@RequestPart MultipartFile file) {
        var res = attachmentService.upload(file);
        var location = UriComponentsBuilder.fromPath("/v1/files")
                .path("/" + res.getId())
                .build()
                .toUri();

        return ResponseEntity.created(location).body(res);
    }

    @Operation(summary = "Download file", parameters = {
            @Parameter(name = "id", description = "Unique identifier of file"),
    })
    @ApiResponse(responseCode = "200", description = "Starting to download")
    @ApiResponse(responseCode = "404", description = "File not exists by given identifier")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Resource> download(@PathVariable UUID id) {
        return attachmentService.download(id).toResponseEntity();
    }

    @Operation(summary = "Delete file", parameters = {
            @Parameter(name = "id", description = "Unique identifier of file"),
    })
    @ApiResponse(responseCode = "204", description = "Deleted successfully")
    @ApiResponse(responseCode = "404", description = "File not exists by given identifier")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        attachmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
