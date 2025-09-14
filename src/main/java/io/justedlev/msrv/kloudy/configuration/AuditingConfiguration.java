package io.justedlev.msrv.kloudy.configuration;

import io.justedlev.msrv.kloudy.common.JwtSubjectAuditorAware;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditingConfiguration {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new JwtSubjectAuditorAware();
    }

    @Bean
    AuditEventRepository auditEventRepository() {
        return new InMemoryAuditEventRepository();
    }
}
