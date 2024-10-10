package io.justedlev.kloudy.kloudy.configuration;

import io.justedlev.kloudy.common.SaftyModelMapper;
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
