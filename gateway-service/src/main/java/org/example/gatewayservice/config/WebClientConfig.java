package org.example.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WebClientConfig {

    @Value("${dynamic.routing.url:http://localhost:8085/routes}")
    private String routingUrl;

    @Bean
    public WebClient routeWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(routingUrl)
                .build();
    }
}