package io.justedlev.msrv.kloudy.common.mapper;

import io.justedlev.msrv.kloudy.model.KloudyFileResponse;
import io.justedlev.msrv.kloudy.repository.entity.FileMetadata;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;

import java.time.LocalDateTime;

@Mapper(config = MapperConfiguration.class, imports = LocalDateTime.class)
public abstract class KloudyFileResponseMapper {
    @Setter(onMethod_ = @Autowired)
    protected ConversionService conversionService;

    @Mapping(target = "createdAt", expression = "java(conversionService.convert(source.getCreatedAt(), LocalDateTime.class))")
    @Mapping(target = "modifiedAt", expression = "java(conversionService.convert(source.getModifiedAt(), LocalDateTime.class))")
    @Mapping(target = "contentType", expression = "java(String.valueOf(source.getContentType()))")
    public abstract KloudyFileResponse map(FileMetadata source);

    public PagedModel<KloudyFileResponse> map(Page<FileMetadata> source) {

        if (source == null) {
            return new PagedModel<>(Page.empty());
        }

        return new PagedModel<>(source.map(this::map));
    }
}
