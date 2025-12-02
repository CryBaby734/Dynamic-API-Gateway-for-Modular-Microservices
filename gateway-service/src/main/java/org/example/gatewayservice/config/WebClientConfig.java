package org.example.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient routeWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:8085/api/routes")
                .build();
    }
}