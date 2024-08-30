package dev.justedlev.kloudy.configuration.security;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private static final String PREFERRED_USERNAME = "preferred_username";

    private final KeycloakGrantedAuthoritiesConverter keycloakGrantedAuthoritiesConverter =
            new KeycloakGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        var name = extractName(jwt);
        var authorities = keycloakGrantedAuthoritiesConverter.convert(jwt);

        return new JwtAuthenticationToken(jwt, authorities, name);
    }

    private String extractName(@NonNull Jwt jwt) {
        return Optional.of(PREFERRED_USERNAME).map(jwt::getClaimAsString).orElseGet(jwt::getSubject);
    }
}
