package io.justedlev.msrv.kloudy.configuration;

import io.justedlev.msrv.kloudy.configuration.properties.KloudyConfigurationProperties;
import io.justedlev.msrv.kloudy.configuration.properties.KloudyStoreConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SystemUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;

@Slf4j
@Configuration
@ConditionalOnBooleanProperty(prefix = KloudyConfigurationProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties({
        KloudyConfigurationProperties.class,
        KloudyStoreConfigurationProperties.class,
        KloudyStoreConfigurationProperties.class,
})
public class KloudyConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CommandLineRunner kloudyStoreInitializer(KloudyStoreConfigurationProperties props) {
        return args -> {

            if (Files.exists(props.getBucket())) {
                log.info("Kloudy bucket detected: {}", props.getBucket().toAbsolutePath().toUri());
                return;
            }

            if (SystemUtils.IS_OS_WINDOWS) {
                var root = Files.createDirectory(props.getBucket());
                log.info("Kloudy bucket created: {}", root.toUri());
            } else {
                var perms = PosixFilePermissions.fromString(props.getPermissions());
                var attrs = PosixFilePermissions.asFileAttribute(perms);
                var root = Files.createDirectory(props.getBucket(), attrs);
                log.info("Kloudy bucket: {} {}", props.getPermissions(), root.toUri());
            }

        };
    }

}
