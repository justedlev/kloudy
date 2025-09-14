package io.justedlev.msrv.kloudy.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Component
public class InstantToLocalDateTime implements Converter<Instant, LocalDateTime> {
    @Nullable
    @Override
    public LocalDateTime convert(@Nullable Instant source) {
        return Optional.ofNullable(source)
                .map(v -> LocalDateTime.ofInstant(v, ZoneId.systemDefault()))
                .orElse(null);
    }
}
