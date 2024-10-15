package io.justedlev.kloudy.kloudy.service;

import io.justedlev.kloudy.kloudy.model.DownloadResponse;
import io.justedlev.kloudy.kloudy.model.KloudyFileFilterParams;
import io.justedlev.kloudy.kloudy.model.KloudyFileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface KloudyFileService {

    KloudyFileResponse upload(MultipartFile file);

    KloudyFileResponse getOne(UUID id);

    Page<KloudyFileResponse> findAll(KloudyFileFilterParams params, Pageable pageable);

    void delete(UUID id);

    DownloadResponse download(UUID id);

}
