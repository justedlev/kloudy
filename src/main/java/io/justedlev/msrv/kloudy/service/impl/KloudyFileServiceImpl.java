package io.justedlev.msrv.kloudy.service.impl;

import io.justedlev.msrv.kloudy.common.mapper.DownloadResponseMapper;
import io.justedlev.msrv.kloudy.common.mapper.KloudyFileResponseMapper;
import io.justedlev.msrv.kloudy.converter.MultipartFileToFileMetadata;
import io.justedlev.msrv.kloudy.model.DownloadResponse;
import io.justedlev.msrv.kloudy.model.KloudyFileFilterParams;
import io.justedlev.msrv.kloudy.model.KloudyFileResponse;
import io.justedlev.msrv.kloudy.repository.FileMetadataRepository;
import io.justedlev.msrv.kloudy.repository.KloudyContentRepository;
import io.justedlev.msrv.kloudy.repository.entity.FileMetadata;
import io.justedlev.msrv.kloudy.repository.specs.FileMetadataSpecifications;
import io.justedlev.msrv.kloudy.service.KloudyFileService;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class KloudyFileServiceImpl implements KloudyFileService {
    private final FileMetadataRepository fileMetadataRepository;
    private final KloudyContentRepository contentRepository;
    private final MultipartFileToFileMetadata multipartFileToFileMetadata;
    private final KloudyFileResponseMapper mapper;
    private final DownloadResponseMapper downloadResponseMapper;

    @SneakyThrows
    @Transactional
    @Override
    public KloudyFileResponse upload(@NonNull MultipartFile file) {
        var entity = Optional.of(file)
                .map(multipartFileToFileMetadata::convert)
                .map(fileMetadataRepository::save)
                .orElseThrow();
        contentRepository.set(entity.getId(), file.getInputStream());
        return mapper.map(entity);
    }

    @Override
    public KloudyFileResponse getOne(UUID id) {
        return fileMetadataRepository.findById(id)
                .map(mapper::map)
                .orElseThrow(notFound(id));
    }

    @Override
    public PagedModel<KloudyFileResponse> findAll(KloudyFileFilterParams params, Pageable pageable) {
        var spec = FileMetadataSpecifications.freeSearch(params.getQ());
        var page = fileMetadataRepository.findAll(spec, pageable);

        return mapper.map(page);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        Try.of(CheckedFunction0.constant(id))
                .map(fileMetadataRepository::findById)
                .mapTry(opt -> opt.orElseThrow(notFound(id)))
                .map(FileMetadata::getId)
                .filter(contentRepository::delete)
                .andFinallyTry(() -> fileMetadataRepository.deleteById(id))
                .onFailure(ex -> log.error("Failed delete file", ex));
    }

    @Override
    public DownloadResponse download(UUID id) {
        var entity = fileMetadataRepository.findById(id)
                .orElseThrow(notFound(id));
        var res = downloadResponseMapper.map(entity);
        res.setResource(contentRepository.get(entity.getId()));

        return res;
    }

    private Supplier<RuntimeException> notFound(UUID id) {
        return () -> new EntityNotFoundException(String.format("File '%s' not found", id));
    }
}
