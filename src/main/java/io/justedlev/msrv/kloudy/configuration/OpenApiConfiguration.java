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
                title = "OpenApi specification - Kloudy APIs",
                description = "Kloudy APIs Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Justedlev",
                        email = "jvstedlev@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                )
        ),
        servers = {
                @Server(
                        description = "Api Gateway Localhost",
                        url = "http://localhost:8765/${spring.application.name:}${server.servlet.context-path:}"
                ),
                @Server(
                        description = "Localhost ENV",
                        url = "http://localhost:${server.port:0}${server.servlet.context-path:}"
                ),
        },
        security = {
                @SecurityRequirement(name = OpenApiConfiguration.OAUTH2),
        }
)
@SecurityScheme(
        name = OpenApiConfiguration.OAUTH2,
        description = "OAuth2",
        openIdConnectUrl = "${KC_OIDC_URI}",
        scheme = StandardAuthScheme.BEARER,
        type = SecuritySchemeType.OPENIDCONNECT,
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfiguration { // NOSONAR
    public static final String OAUTH2 = "oauth2";
}
