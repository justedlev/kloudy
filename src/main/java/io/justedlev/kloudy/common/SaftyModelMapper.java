package io.justedlev.kloudy.common;

import io.justedlev.commons.jtc4mm.LocalDateTimeToTimestamp;
import io.justedlev.commons.jtc4mm.TimestampToLocalDateTime;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class SaftyModelMapper extends ModelMapper {
    public SaftyModelMapper() {
        this.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);
        addConverter(new TimestampToLocalDateTime());
        addConverter(new LocalDateTimeToTimestamp());
    }

    @Override
    public <D> D map(Object source, Class<D> destination) {
        return source == null ? null : super.map(source, destination);
    }
}
