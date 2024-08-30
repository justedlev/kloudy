package dev.justedlev.kloudy.service;

import dev.justedlev.kloudy.model.KloudyFileResponse;
import dev.justedlev.kloudy.model.AttachmentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface AttachmentService {
    KloudyFileResponse upload(MultipartFile file);

    void delete(UUID id);

    AttachmentResponse download(UUID id);
}
