package io.justedlev.msrv.kloudy.service.impl;

import io.justedlev.msrv.kloudy.converter.KloudyFileToResponse;
import io.justedlev.msrv.kloudy.converter.MultipartFileToAttachment;
import io.justedlev.msrv.kloudy.configuration.properties.KloudyProperties;
import io.justedlev.msrv.kloudy.model.DownloadResponse;
import io.justedlev.msrv.kloudy.model.KloudyFileFilterParams;
import io.justedlev.msrv.kloudy.model.KloudyFileResponse;
import io.justedlev.msrv.kloudy.repository.KloudyFileRepository;
import io.justedlev.msrv.kloudy.repository.entity.KloudyFile;
import io.justedlev.msrv.kloudy.service.KloudyFileService;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class KloudyFileServiceImpl implements KloudyFileService {
    private final KloudyFileRepository kloudyFileRepository;
    private final KloudyProperties properties;
    private final MultipartFileToAttachment multipartFileToAttachment;
    private final KloudyFileToResponse kloudyFileToResponse;

    @SneakyThrows
    @Transactional
    @Override
    public KloudyFileResponse upload(@NonNull MultipartFile file) {
        var entity = Optional.of(file)
                .map(multipartFileToAttachment::convert)
                .map(kloudyFileRepository::save)
                .orElseThrow();
        var copyLocation = properties.getRootPath().resolve(entity.id().toString());
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        return kloudyFileToResponse.convert(entity);
    }

    @Override
    public KloudyFileResponse getOne(UUID id) {
        return kloudyFileRepository.findById(id)
                .map(kloudyFileToResponse::convert)
                .orElseThrow(notFound(id));
    }

    @Override
    public Page<KloudyFileResponse> findAll(KloudyFileFilterParams params, Pageable pageable) {
        Specification<KloudyFile> spec = (root, cq, cb) -> null;

        return kloudyFileRepository.findAll(spec, pageable)
                .map(kloudyFileToResponse::convert);
    }

    @SneakyThrows
    @Transactional
    @Override
    public void delete(UUID id) {
        Try.of(CheckedFunction0.constant(id))
                .map(kloudyFileRepository::findById)
                .mapTry(opt -> opt.orElseThrow(notFound(id)))
                .map(file -> Pair.of(file.id(), properties.getRootPath().resolve(file.id().toString())))
                .filter(pair -> Files.exists(pair.getRight()))
                .andThenTry(pair -> Files.delete(pair.getRight()))
                .onFailure(ex -> log.error("Failed delete file", ex))
                .andFinally(() -> kloudyFileRepository.deleteById(id));
    }

    @Override
    public DownloadResponse download(UUID id) {
        var entity = kloudyFileRepository.findById(id)
                .orElseThrow(notFound(id));
        var path = properties.getRootPath().resolve(entity.id().toString());

        return DownloadResponse.builder()
                .filename(entity.getFilename())
                .extension(entity.getExtension())
                .resource(new FileSystemResource(path))
                .length(entity.getLength())
                .contentType(MediaType.parseMediaType(entity.getType()))
                .build();
    }

    private Supplier<RuntimeException> notFound(UUID id) {
        return () -> new EntityNotFoundException("Kloudy file not found: " + id);
    }
}
