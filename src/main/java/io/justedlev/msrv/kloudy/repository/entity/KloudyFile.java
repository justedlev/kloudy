package io.justedlev.msrv.kloudy.repository.entity;

import io.justedlev.sb3c.AbstractPersistable;
import io.justedlev.sb3c.Auditable;
import io.justedlev.sb3c.Versionable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@With
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Audited
@AuditOverride(forClass = Versionable.class)
@AuditOverride(forClass = Auditable.class)
@AuditOverride(forClass = AbstractPersistable.class)
@Entity
@DynamicUpdate
@Table(name = "file")
@ToString
public class KloudyFile extends Versionable<UUID> implements Serializable {

    @NotBlank
    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "extension")
    private String extension;

    @NotBlank
    @Column(name = "type", nullable = false)
    private String type;

    @Min(1)
    @Column(name = "length", nullable = false)
    private Long length;

}
