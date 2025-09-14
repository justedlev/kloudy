package io.justedlev.msrv.kloudy.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties(KloudyConfigurationProperties.PREFIX)
public class KloudyConfigurationProperties {
    public static final String PREFIX = "kloudy";
    private boolean enabled = true;
    @NestedConfigurationProperty
    private KloudyStoreConfigurationProperties store;
    @NestedConfigurationProperty
    private KloudySecurityConfigurationProperties security;
}
