package io.justedlev.msrv.kloudy.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@ConditionalOnBooleanProperty(prefix = "spring.cache.redis", name = "enabled", matchIfMissing = true)
public class RedisCacheConfiguration {
}
