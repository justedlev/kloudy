package dev.justedlev.kloudy.configuration;

import dev.justedlev.common.SaftyModelMapper;
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
