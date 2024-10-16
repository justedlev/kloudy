package io.justedlev.msrv.kloudy.configuration;

import io.justedlev.msrv.kloudy.common.SaftyModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {
    @Bean
    ModelMapper modelMapper() {
        return new SaftyModelMapper();
    }
}
