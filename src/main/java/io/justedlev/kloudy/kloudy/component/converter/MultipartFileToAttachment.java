package io.justedlev.kloudy.kloudy.component.converter;

import io.justedlev.kloudy.kloudy.repository.entity.KloudyFile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
public class MultipartFileToAttachment implements Converter<MultipartFile, KloudyFile> {
    @Nullable
    @Override
    public KloudyFile convert(@Nullable MultipartFile source) {

        if (Objects.isNull(source)) {
            return null;
        }

        return KloudyFile.builder()
                .filename(source.getOriginalFilename())
                .extension(StringUtils.getFilenameExtension(source.getOriginalFilename()))
                .type(source.getContentType())
                .length(source.getSize())
                .build();
    }
}
