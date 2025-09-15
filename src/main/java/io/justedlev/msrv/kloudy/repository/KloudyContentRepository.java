package io.justedlev.msrv.kloudy.repository;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class KloudyContentRepository extends SimpleContentRepository<UUID> implements ContentRepository<UUID> {
}
