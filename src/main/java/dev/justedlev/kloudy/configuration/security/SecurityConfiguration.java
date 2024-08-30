package dev.justedlev.kloudy.configuration.security;

import dev.justedlev.kloudy.configuration.properties.SecurityProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final SecurityProperties properties;

    @Bean
    public SecurityFilterChain securityFilterChain(@NonNull HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(serverSpec -> serverSpec
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())
                        )
                )
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                        .requestMatchers(properties.getWhiteList().toArray(String[]::new)).permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/files/**").hasAnyAuthority("SCOPE_kloudy.files:read", "ROLE_user")
                        .requestMatchers(HttpMethod.POST, "/v1/files/**").hasAnyAuthority("SCOPE_kloudy.files:write", "ROLE_user")
                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }
}
