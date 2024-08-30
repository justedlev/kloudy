package dev.justedlev.kloudy.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attachment")
@ToString
public class Attachment extends Auditable implements Serializable {
    @Id
    @Column(name = "id")
    @UuidGenerator
    @Setter(AccessLevel.PROTECTED)
    private UUID id;
    @Column(name = "filename", nullable = false)
    private String filename;
    @Column(name = "extension")
    private String extension;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "length", nullable = false)
    private Long length;
}
