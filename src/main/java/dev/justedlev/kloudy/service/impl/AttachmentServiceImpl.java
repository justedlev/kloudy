package dev.justedlev.kloudy.service.impl;

import dev.justedlev.kloudy.component.MultipartFileToAttachment;
import dev.justedlev.kloudy.configuration.properties.KloudyProperties;
import dev.justedlev.kloudy.model.AttachmentResponse;
import dev.justedlev.kloudy.model.KloudyFileResponse;
import dev.justedlev.kloudy.repository.AttachmentRepository;
import dev.justedlev.kloudy.repository.entity.Attachment;
import dev.justedlev.kloudy.service.AttachmentService;
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
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final KloudyProperties properties;
    private final MultipartFileToAttachment multipartFileToAttachment;

    @SneakyThrows
    @Transactional
    @Override
    public KloudyFileResponse upload(@NonNull MultipartFile file) {
        var attachment = Optional.of(file)
                .map(multipartFileToAttachment::convert)
                .map(attachmentRepository::save)
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
        var entity = attachmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("File %s not found", id)));
        delete(entity);
    }

    @SneakyThrows
    private void delete(Attachment entity) {
        var path = Optional.of(entity)
                .map(Attachment::getId)
                .map(UUID::toString)
                .map(properties.getRootPath()::resolve)
                .filter(Files::exists)
                .orElseThrow(() -> new EntityNotFoundException(String.format("File %s not found", entity.getId())));

        if (Files.exists(path)) {
            Files.delete(path);
        }

        attachmentRepository.deleteById(entity.getId());
    }

    @Override
    public AttachmentResponse download(UUID id) {
        Attachment entity = attachmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("File %s not found", id)));
        var path = properties.getRootPath().resolve(entity.getId().toString());

        if (!Files.exists(path)) {
            attachmentRepository.delete(entity);
            throw new EntityNotFoundException(String.format("File %s not found", entity.getId()));
        }

        var file = path.toFile();

        return AttachmentResponse.builder()
                .filename(entity.getFilename())
                .extension(entity.getExtension())
                .resource(new FileSystemResource(file))
                .length(file.length())
                .contentType(MediaType.parseMediaType(entity.getType()))
                .build();
    }
}
