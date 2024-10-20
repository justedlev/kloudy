package io.justedlev.msrv.kloudy.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.apache.hc.client5.http.auth.StandardAuthScheme;
import org.springframework.context.annotation.Configuration;

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
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                )
        ),
        servers = {
                @Server(
                        description = "Localhost ENV",
                        url = "http://localhost:8765/${spring.application.name}${server.servlet.context-path}"
                ),
        },
        security = {
                @SecurityRequirement(name = OpenApiConfiguration.OAUTH2),
        }
)
@SecurityScheme(
        name = OpenApiConfiguration.OAUTH2,
        description = "OAuth 2",
        openIdConnectUrl = "${keycloak.oidc-uri}",
        scheme = StandardAuthScheme.BEARER,
        type = SecuritySchemeType.OPENIDCONNECT,
        in = SecuritySchemeIn.HEADER
//        flows = @OAuthFlows(
//                authorizationCode = @OAuthFlow(
//                        authorizationUrl = "${keycloak.auth-uri}",
//                        tokenUrl = "http://localhost:8765/sso/oauth2/token",
//                        refreshUrl = "http://localhost:8765/sso/oauth2/token"
//                ),
//                clientCredentials = @OAuthFlow(
//                        authorizationUrl = "${keycloak.auth-uri}",
//                        tokenUrl = "http://localhost:8765/sso/oauth2/token",
//                        refreshUrl = "http://localhost:8765/sso/oauth2/token",
//                        scopes = {
//                                @OAuthScope(name = "openid"),
//                                @OAuthScope(name = "profile"),
//                                @OAuthScope(name = "email"),
//                                @OAuthScope(name = "kloudy.files:read"),
//                                @OAuthScope(name = "kloudy.files:write"),
//                        }
//                )
//        )
)
@Configuration
public class OpenApiConfiguration { // NOSONAR
    public static final String OAUTH2 = "oauth2";
}
