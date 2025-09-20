package io.justedlev.msrv.kloudy.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@ConditionalOnBooleanProperty(prefix = "spring.security.oauth2.client", name = "enabled", matchIfMissing = true)
@Import(OAuth2ClientAutoConfiguration.class)
public class OAuth2ClientConfiguration {
    @Getter
    @Setter(onMethod_ = @Value("${spring.security.oauth2.client.enabled}"))
    private boolean enabled = true;

    public OAuth2ClientConfiguration() {
        log.info("OAuth2ClientConfiguration has been initialized");
    }
}
