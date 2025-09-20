package io.justedlev.msrv.kloudy.configuration;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@ConditionalOnBooleanProperty(prefix = "spring.rabbitmq", name = "enabled", matchIfMissing = true)
@Import(RabbitAutoConfiguration.class)
public class RabbitMQConfiguration {
    @Setter(onMethod_ = @Value("${spring.rabbitmq.enabled}"))
    private boolean enabled;

    public RabbitMQConfiguration() {
        log.info("RabbitMQ configuration has been initialized: {}", enabled);
    }
}
