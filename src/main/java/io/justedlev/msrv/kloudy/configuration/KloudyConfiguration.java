package io.justedlev.msrv.kloudy.configuration;

import io.justedlev.msrv.kloudy.common.JwtSubjectAuditorAware;
import io.justedlev.msrv.kloudy.common.SaftyModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class KloudyConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new SaftyModelMapper();
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new JwtSubjectAuditorAware();
    }

}
