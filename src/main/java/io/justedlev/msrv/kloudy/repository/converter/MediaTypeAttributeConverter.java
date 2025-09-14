package io.justedlev.msrv.kloudy.repository.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;

import java.util.Optional;

@Converter(autoApply = true)
public class MediaTypeAttributeConverter implements AttributeConverter<MediaType, String> {
    @Nullable
    @Override
    public String convertToDatabaseColumn(@Nullable MediaType attribute) {
        return Optional.ofNullable(attribute).map(MediaType::toString).orElse(null);
    }

    @Nullable
    @Override
    public MediaType convertToEntityAttribute(@Nullable String dbData) {
        return Optional.ofNullable(dbData).map(MediaType::valueOf).orElse(null);
    }
}
