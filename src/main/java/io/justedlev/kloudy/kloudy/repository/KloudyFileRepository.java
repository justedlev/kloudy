package io.justedlev.kloudy.kloudy.repository;

import io.justedlev.kloudy.kloudy.repository.entity.KloudyFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.UUID;

public interface KloudyFileRepository
        extends JpaRepository<KloudyFile, UUID>,
        JpaSpecificationExecutor<KloudyFile>,
        RevisionRepository<KloudyFile, UUID, Integer> {
}
