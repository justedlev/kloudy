package io.justedlev.kloudy.kloudy.service.impl;

import io.justedlev.kloudy.kloudy.component.MultipartFileToAttachment;
import io.justedlev.kloudy.kloudy.configuration.properties.KloudyProperties;
import io.justedlev.kloudy.kloudy.model.DownloadResponse;
import io.justedlev.kloudy.kloudy.model.KloudyFileResponse;
import io.justedlev.kloudy.kloudy.repository.KloudyFileRepository;
import io.justedlev.kloudy.kloudy.repository.entity.KloudyFile;
import io.justedlev.kloudy.kloudy.service.KloudyFileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KloudyFileServiceImpl implements KloudyFileService {
    private final KloudyFileRepository kloudyFileRepository;
    private final KloudyProperties properties;
    private final MultipartFileToAttachment multipartFileToAttachment;

    @SneakyThrows
    @Transactional
    @Override
    public KloudyFileResponse upload(@NonNull MultipartFile file) {
        var attachment = Optional.of(file)
                .map(multipartFileToAttachment::convert)
                .map(kloudyFileRepository::save)
                .orElseThrow();
        var copyLocation = properties.getRootPath().resolve(attachment.getId().toString());
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        return KloudyFileResponse.builder()
                .id(attachment.getId())
                .mimeType(attachment.getType())
                .extension(attachment.getExtension())
                .filename(attachment.getFilename())
                .length(attachment.getLength())
                .build();
    }

    @SneakyThrows
    @Transactional
    @Override
    public void delete(UUID id) {
        var entity = kloudyFileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("File %s not found", id)));
        delete(entity);
    }

    @SneakyThrows
    private void delete(KloudyFile entity) {
        var path = Optional.of(entity)
                .map(KloudyFile::getId)
                .map(UUID::toString)
                .map(properties.getRootPath()::resolve)
                .filter(Files::exists)
                .orElseThrow(() -> new EntityNotFoundException(String.format("File %s not found", entity.getId())));

        if (Files.exists(path)) {
            Files.delete(path);
        }

        kloudyFileRepository.deleteById(entity.getId());
    }

    @Override
    public DownloadResponse download(UUID id) {
        var entity = kloudyFileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("File %s not found", id)));
        var path = properties.getRootPath().resolve(entity.getId().toString());

        if (!Files.exists(path)) {
            kloudyFileRepository.delete(entity);
            throw new EntityNotFoundException(String.format("File %s not found", entity.getId()));
        }

        var file = path.toFile();

        return DownloadResponse.builder()
                .filename(entity.getFilename())
                .extension(entity.getExtension())
                .resource(new FileSystemResource(file))
                .length(file.length())
                .contentType(MediaType.parseMediaType(entity.getType()))
                .build();
    }
}
