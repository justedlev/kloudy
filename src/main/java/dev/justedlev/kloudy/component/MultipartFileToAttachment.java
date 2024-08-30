package dev.justedlev.kloudy.component;

import dev.justedlev.kloudy.repository.entity.Attachment;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
public class MultipartFileToAttachment implements Converter<MultipartFile, Attachment> {
    @Nullable
    @Override
    public Attachment convert(@Nullable MultipartFile source) {

        if (Objects.isNull(source)) {
            return null;
        }

        return Attachment.builder()
                .filename(source.getOriginalFilename())
                .extension(StringUtils.getFilenameExtension(source.getOriginalFilename()))
                .type(source.getContentType())
                .length(source.getSize())
                .build();
    }
}
