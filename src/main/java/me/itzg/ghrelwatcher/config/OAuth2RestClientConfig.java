package me.itzg.ghrelwatcher.config;

import lombok.extern.slf4j.Slf4j;
import me.itzg.ghrelwatcher.GithubGraphql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.client.RestTemplate;

/**
 * Exposes a {@link RestTemplate} bean that adds an authentication header for the current OAuth2 client context.
 *
 * @author Geoff Bourne
 * @since May 2018
 */
@Configuration
@Slf4j
public class OAuth2RestClientConfig {

    private RestTemplateBuilder restTemplateBuilder;
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public OAuth2RestClientConfig(RestTemplateBuilder restTemplateBuilder, OAuth2AuthorizedClientService authorizedClientService) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.authorizedClientService = authorizedClientService;
    }

    @Bean
    public RestTemplate restTemplate() {
        return restTemplateBuilder.additionalInterceptors((httpRequest, bytes, clientHttpRequestExecution) -> {
            OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            OAuth2AuthorizedClient authedClient = authorizedClientService.loadAuthorizedClient(
                    authentication.getAuthorizedClientRegistrationId(),
                    authentication.getName());

            String tokenType = authedClient.getAccessToken().getTokenType().getValue();
            String token = authedClient.getAccessToken().getTokenValue();
            final String authHeader = String.format("%s %s", tokenType, token);

            log.debug("Intercepting HTTP request and adding OAuth2 authentication header");
            httpRequest.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);

            return clientHttpRequestExecution.execute(httpRequest, bytes);
        })
                .rootUri(GithubGraphql.ROOT_URL)
                .build();
    }
}
