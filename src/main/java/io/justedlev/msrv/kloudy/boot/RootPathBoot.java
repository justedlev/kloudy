package io.justedlev.msrv.kloudy.boot;

import io.justedlev.msrv.kloudy.configuration.properties.KloudyProperties;
import io.justedlev.msrv.kloudy.repository.KloudyFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;

@Slf4j
@Component
@RequiredArgsConstructor
public class RootPathBoot implements ApplicationRunner {
    private final KloudyProperties properties;
    private final KloudyFileRepository kloudyFileRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (Files.notExists(properties.getRootPath())) {
            Files.createDirectory(properties.getRootPath());
            kloudyFileRepository.deleteAll();
            log.info("Created root directory: {}", properties.getRootPath().toAbsolutePath());
        }
    }
}
