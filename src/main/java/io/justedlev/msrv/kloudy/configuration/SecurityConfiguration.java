package io.justedlev.msrv.kloudy.configuration;

import io.justedlev.msrv.kloudy.configuration.properties.KloudySecurityConfigurationProperties;
import io.justedlev.msrv.kloudy.controller.FilesController;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ExpressionJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;

@Configuration
@EnableConfigurationProperties(KloudySecurityConfigurationProperties.class)
@RequiredArgsConstructor
public class SecurityConfiguration {
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    private static final PropertyMapper PROPERTY_MAPPER = PropertyMapper.get().alwaysApplyingWhenNonNull();
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String SCOPE_PREFIX = "SCOPE_";

    private final KloudySecurityConfigurationProperties kscp;

    @Bean
    SecurityFilterChain securityFilterChain(@NonNull HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(configurer -> configurer.jwt(Customizer.withDefaults()))
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(HttpMethod.GET, "/actuator/prometheus")
                            .hasAuthority(SCOPE_PREFIX + "prometheus.metrics:read");
                    var filesPath = FilesController.CONTEXT_PATH + "/**";
                    registry.requestMatchers(HttpMethod.GET, filesPath)
                            .hasAnyAuthority(
                                    SCOPE_PREFIX + "kloudy.files:ro",
                                    ROLE_PREFIX + "user"
                            );
                    registry.requestMatchers(HttpMethod.POST, filesPath)
                            .hasAnyAuthority(
                                    SCOPE_PREFIX + "kloudy.files:rw",
                                    ROLE_PREFIX + "user"
                            );
                    registry.requestMatchers(HttpMethod.DELETE, filesPath)
                            .hasAnyAuthority(
                                    SCOPE_PREFIX + "kloudy.files:rw",
                                    ROLE_PREFIX + "admin"
                            );
                    kscp.getWhitelist().forEach((k, v) -> registry.requestMatchers(k, v).permitAll());
                    registry.anyRequest().authenticated();
                })
                .build();
    }

    @Bean
    ClientHttpRequestInterceptor oauth2ClientHttpRequestInterceptor(
            OAuth2AuthorizedClientManager authorizedClientManager,
            Environment env
    ) {
        var interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);
        interceptor.setClientRegistrationIdResolver(request -> env.getProperty("spring.application.name"));

        return interceptor;
    }

    @Bean
    RestClientCustomizer configClientOAuth2Customizer(ClientHttpRequestInterceptor oauth2ClientHttpRequestInterceptor) {
        return builder -> builder.requestInterceptor(oauth2ClientHttpRequestInterceptor);
    }

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository registrationRepository,
            OAuth2AuthorizedClientService clientService
    ) {
        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(registrationRepository, clientService);
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(
            OAuth2ResourceServerProperties properties,
            Collection<Converter<Jwt, Collection<GrantedAuthority>>> converters
    ) {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        PROPERTY_MAPPER.from(properties.getJwt()::getPrincipalClaimName).to(jwtAuthenticationConverter::setPrincipalClaimName);
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new DelegatingJwtGrantedAuthoritiesConverter(converters));

        return jwtAuthenticationConverter;
    }

    @Bean
    ExpressionJwtGrantedAuthoritiesConverter resourceAccessRolesJwtGrantedAuthoritiesConverter() {
        var exp = PARSER.parseExpression("[resource_access][[azp][0]][roles]");
        var converter = new ExpressionJwtGrantedAuthoritiesConverter(exp);
        converter.setAuthorityPrefix(ROLE_PREFIX);

        return converter;
    }

    @Bean
    ExpressionJwtGrantedAuthoritiesConverter realmAccessRolesJwtGrantedAuthoritiesConverter() {
        var exp = PARSER.parseExpression("[realm_access][roles]");
        var converter = new ExpressionJwtGrantedAuthoritiesConverter(exp);
        converter.setAuthorityPrefix(ROLE_PREFIX);

        return converter;
    }

    @Bean
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter(OAuth2ResourceServerProperties properties) {
        var converter = new JwtGrantedAuthoritiesConverter();
        PROPERTY_MAPPER.from(properties.getJwt()::getAuthorityPrefix).to(converter::setAuthorityPrefix);
        PROPERTY_MAPPER.from(properties.getJwt()::getAuthoritiesClaimDelimiter).to(converter::setAuthoritiesClaimDelimiter);
        PROPERTY_MAPPER.from(properties.getJwt()::getAuthoritiesClaimName).to(converter::setAuthoritiesClaimName);

        return converter;
    }

}
