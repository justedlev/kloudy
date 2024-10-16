package io.justedlev.msrv.kloudy.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@Setter
@Getter
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private URI serverUrl;
    private URI issuerUri;
    private URI jwkSetUri;
    private URI introspectionUri;
    private URI tokenUri;
    private URI authUri;
    private URI oidcUri;
    private String realm;
}
