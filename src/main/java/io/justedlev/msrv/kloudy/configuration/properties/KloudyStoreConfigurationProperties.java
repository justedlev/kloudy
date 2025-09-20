package io.justedlev.msrv.kloudy.configuration.properties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@Getter
@Setter
@ConfigurationProperties(KloudyStoreConfigurationProperties.PREFIX)
public class KloudyStoreConfigurationProperties {
    public static final String PREFIX = KloudyConfigurationProperties.PREFIX + ".store";
    /**
     * The location in filesystem for content
     * <p>
     * Default: ${user.home}/.kloudy.bucket.d
     */
    @Setter(onMethod_ = @Value("${user.home}/.kloudy.bucket.d"), onParam_ = @NonNull) // NOSONAR
    private Path bucket;
    /**
     * The posix permissions
     *
     * @see java.nio.file.attribute.PosixFilePermission
     * @see java.nio.file.attribute.PosixFilePermissions
     */
    private String permissions = "rwxr-x---";
}
