package io.justedlev.msrv.kloudy.autoconfigure;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableCaching
@ConditionalOnBooleanProperty(prefix = "spring.cache.redis", name = "enabled", matchIfMissing = true)
@Import(RedisAutoConfiguration.class)
public class KloudyRedisCacheAutoConfiguration {
    @Getter
    @Value("${spring.cache.redis.enabled:false}")
    private boolean enabled;
}
