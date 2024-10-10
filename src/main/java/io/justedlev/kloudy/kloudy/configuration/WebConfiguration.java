package io.justedlev.kloudy.kloudy.configuration;

import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry
                .addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name()
                )
                .allowedHeaders(
                        HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
                        HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                        HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                        HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN
                )
                .exposedHeaders(
                        HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS
                );
    }
}
