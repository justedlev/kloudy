package io.justedlev.kloudy.kloudy.service;

import io.justedlev.kloudy.kloudy.model.DownloadResponse;
import io.justedlev.kloudy.kloudy.model.KloudyFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface KloudyFileService {
    KloudyFileResponse upload(MultipartFile file);

    void delete(UUID id);

    DownloadResponse download(UUID id);
}
