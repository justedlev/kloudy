package dev.justedlev.kloudy.configuration.security;

import com.jayway.jsonpath.JsonPath;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class KeycloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final String ROLE_PREFIX = "ROLE_";
    private static final Set<String> WELL_KNOWN_AUTHORITIES_CLAIM_NAMES =
            Set.of("$.realm_access.roles", "$.resource_access.account.roles");

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        var grantedAuthorities = jwtGrantedAuthoritiesConverter.convert(jwt);
        var keycloakRoles = extractKeycloakRoles(jwt);

        return Stream.of(grantedAuthorities, keycloakRoles)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Collection<GrantedAuthority> extractKeycloakRoles(Jwt jwt) {
        return WELL_KNOWN_AUTHORITIES_CLAIM_NAMES.stream()
                .map(path -> readFromClaims(jwt.getClaims(), path))
                .flatMap(Collection::stream)
                .map(this::toGrantAuthority)
                .toList();
    }

    private Collection<String> readFromClaims(Map<String, Object> claims, String path) {
        return Try.of(CheckedFunction0.constant(claims))
                .mapTry(value -> JsonPath.read(value, path))
                .filter(Collection.class::isInstance)
                .mapTry(this::castToCollectionOfStrings)
                .onFailure(e -> log.error(e.getMessage(), e))
                .getOrElseGet(e -> List.of());
    }

    private GrantedAuthority toGrantAuthority(String authority) {
        return new SimpleGrantedAuthority(ROLE_PREFIX + authority);
    }

    @SuppressWarnings("unchecked")
    private Collection<String> castToCollectionOfStrings(@Nullable Object value) {
        return Optional.ofNullable(value)
                .map(current -> (Collection<String>) current)
                .orElse(List.of());
    }
}
