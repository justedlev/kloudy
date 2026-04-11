package io.justedlev.msrv.kloudy.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;

@AutoConfiguration
@ConditionalOnBooleanProperty(prefix = "spring.security.oauth2.client", name = "enabled", matchIfMissing = true)
@Import(OAuth2ClientAutoConfiguration.class)
public class KloudyOAuth2ClientAutoConfiguration {
    @Getter
    @Setter(onMethod_ = @Value("${spring.security.oauth2.client.enabled:true}"))
    private boolean enabled;

    @Bean
    @ConditionalOnMissingBean
    OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository registrationRepository,
            OAuth2AuthorizedClientService clientService
    ) {
        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(registrationRepository, clientService);
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

}
