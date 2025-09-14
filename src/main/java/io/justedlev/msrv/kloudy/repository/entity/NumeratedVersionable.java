package io.justedlev.msrv.kloudy.repository.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class NumeratedVersionable<K extends Serializable> extends DefaultAuditable<K> { // NOSONAR
    @Version
    private Long version;
}
