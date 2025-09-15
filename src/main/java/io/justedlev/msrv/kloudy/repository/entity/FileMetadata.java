package io.justedlev.msrv.kloudy.repository.entity;

import io.justedlev.msrv.kloudy.model.Attributable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.http.MediaType;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Audited
@AuditOverride(forClass = NumeratedVersionable.class)
@AuditOverride(forClass = DefaultAuditable.class)
@AuditOverride(forClass = AbstractPersistable.class)
@Entity
@DynamicUpdate
@Table(name = "file_metadata")
public class FileMetadata extends NumeratedVersionable<UUID> implements Attributable<String>, Serializable { // NOSONAR
    @NotBlank
    @Column(name = "filename", nullable = false)
    private String filename;
    @Column(name = "extension")
    private String extension;
    @NotNull
    @Column(name = "content_type", nullable = false)
    private MediaType contentType;
    @Min(1)
    @Column(name = "length", nullable = false)
    private Long length;
    @Singular
    @Fetch(FetchMode.SUBSELECT)
    @ElementCollection
    @MapKeyColumn(name = "name")
    @Column(name = "_value", nullable = false)
    @CollectionTable(name = "file_metadata_attributes", joinColumns = @JoinColumn(name = "file_metadata_id"))
    private Map<@NotBlank String, @NotNull String> attributes;
}
