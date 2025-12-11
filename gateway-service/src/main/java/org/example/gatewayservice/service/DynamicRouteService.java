package org.example.gatewayservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gatewayservice.converter.RouteDtoToSpringConverter;
import org.example.gatewayservice.dto.RouteDefinitionDto;
import org.example.gatewayservice.dto.RouteDefinitionResponse;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;
@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicRouteService {

    private final WebClient routeWebClient;
    private final RouteDtoToSpringConverter converter;

    public Flux<RouteDefinition> fetchActiveRoutes() {
        log.info("Fetching active routes from dynamic-routing-core...");

        return routeWebClient
                .get()
                .uri("/definitions")
                .retrieve()
                .bodyToFlux(RouteDefinitionDto.class)
                .map(converter::convert);
    }
}