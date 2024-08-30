package dev.justedlev.kloudy.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.servers.Server;
import org.apache.hc.client5.http.auth.StandardAuthScheme;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Justedlev",
                        email = "jvstedlev@gmail.com"
                ),
                description = "Kloudy APIs Documentation",
                title = "OpenApi specification - Kloudy APIs",
                version = "v1",
                license = @License(
                        name = "Apache 2.0",
                        url = "https://springdoc.org"
                )
        ),
        servers = {
                @Server(
                        description = "Localhost ENV",
                        url = "http://localhost:8765/${spring.application.name}${server.servlet.context-path}"
                ),
        },
        security = {
                @SecurityRequirement(name = "OAuth 2"),
        }
)
@SecurityScheme(
        name = "OAuth 2",
        description = "OAuth 2",
        openIdConnectUrl = "${keycloak.oidc-url}",
        scheme = StandardAuthScheme.BEARER,
        type = SecuritySchemeType.OPENIDCONNECT,
        in = SecuritySchemeIn.HEADER,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "${keycloak.auth-uri}",
                        tokenUrl = "${keycloak.token-uri}",
                        scopes = {
                                @OAuthScope(name = "openid"),
                                @OAuthScope(name = "profile"),
                                @OAuthScope(name = "email"),
                                @OAuthScope(name = "kloudy.files:read"),
                                @OAuthScope(name = "kloudy.files:write"),
                        }
                )
        )
)
public class OpenApiConfiguration {
}
