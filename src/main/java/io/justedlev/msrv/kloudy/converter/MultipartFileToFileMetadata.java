package io.justedlev.msrv.kloudy.converter;

import io.justedlev.msrv.kloudy.repository.entity.FileMetadata;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
public class MultipartFileToFileMetadata implements Converter<MultipartFile, FileMetadata> {
    @Nullable
    @Override
    public FileMetadata convert(@Nullable MultipartFile source) {

        if (Objects.isNull(source)) {
            return null;
        }

        return FileMetadata.builder()
                .filename(source.getOriginalFilename())
                .extension(StringUtils.getFilenameExtension(source.getOriginalFilename()))
                .contentType(MediaType.valueOf(source.getContentType()))
                .length(source.getSize())
                .build();
    }
}
