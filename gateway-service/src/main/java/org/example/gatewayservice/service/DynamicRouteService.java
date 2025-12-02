package org.example.gatewayservice.service;

import lombok.RequiredArgsConstructor;
import org.example.gatewayservice.converter.RouteDtoToSpringConverter;
import org.example.gatewayservice.dto.RouteDefinitionResponse;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class DynamicRouteService {

    private final WebClient routeWebClient;
    private final RouteDtoToSpringConverter converter;

    public Flux<RouteDefinition> fetchActiveRoutes() {
        return routeWebClient
                .get()
                // 👇 договоримся, что в core есть такой endpoint:
                .uri("/active")
                .retrieve()
                .bodyToFlux(RouteDefinitionResponse.class)
                .map(converter::convert);
    }
}
