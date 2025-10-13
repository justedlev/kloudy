package io.justedlev.msrv.kloudy.configuration;

import io.justedlev.msrv.kloudy.configuration.properties.KloudySecurityConfigurationProperties;
import io.justedlev.msrv.kloudy.controller.FilesController;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.ConditionalOnOAuth2ClientRegistrationProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@ConditionalOnOAuth2ClientRegistrationProperties
@EnableConfigurationProperties(KloudySecurityConfigurationProperties.class)
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final KloudySecurityConfigurationProperties props;
    @Setter
    private String contextPath = "";

    @Autowired
    protected void setContextPathFrom(ServerProperties props) {
        Optional.of(props)
                .map(ServerProperties::getServlet)
                .map(ServerProperties.Servlet::getContextPath)
                .ifPresent(this::setContextPath);
    }

    @Bean
    SecurityFilterChain securityFilterChain(@NonNull HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(configurer -> configurer.jwt(Customizer.withDefaults()))
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(HttpMethod.GET, contextPath + "/actuator/prometheus")
                            .hasAuthority(props.getScopePrefix() + "prometheus.metrics:ro");
                    var filesPath = contextPath + FilesController.CONTEXT_PATH + "/**";
                    registry.requestMatchers(HttpMethod.GET, filesPath)
                            .hasAnyAuthority(
                                    props.getScopePrefix() + "kloudy.files:ro",
                                    props.getRolePrefix() + "user"
                            );
                    registry.requestMatchers(HttpMethod.POST, filesPath)
                            .hasAnyAuthority(
                                    props.getScopePrefix() + "kloudy.files:rw",
                                    props.getRolePrefix() + "user"
                            );
                    registry.requestMatchers(HttpMethod.DELETE, filesPath)
                            .hasAnyAuthority(
                                    props.getScopePrefix() + "kloudy.files:rw",
                                    props.getRolePrefix() + "admin"
                            );
                    props.getWhitelist().forEach((k, v) -> registry.requestMatchers(k, v).permitAll());
                    registry.anyRequest().authenticated();
                })
                .build();
    }

}
