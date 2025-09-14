package io.justedlev.msrv.kloudy.common.mapper;

import io.justedlev.msrv.kloudy.model.DownloadResponse;
import io.justedlev.msrv.kloudy.repository.entity.FileMetadata;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;

@Mapper(config = MapperConfiguration.class, imports = MediaType.class)
public abstract class DownloadResponseMapper {
    @Setter(onMethod_ = @Autowired)
    protected ConversionService conversionService;

    @Mapping(target = "resource", ignore = true)
    public abstract DownloadResponse map(FileMetadata source);
}
