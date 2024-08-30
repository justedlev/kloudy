package dev.justedlev.kloudy.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;
import java.nio.file.Path;

@Getter
@Setter
@ConfigurationProperties(prefix = "kloudy")
public class KloudyProperties {
    /**
     * The root path to upload and download files
     */
    private Path rootPath = Path.of(File.separator);
}
