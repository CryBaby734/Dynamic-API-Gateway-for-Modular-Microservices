package org.example.gatewayservice.controller;

import org.example.gatewayservice.dto.RouteDefinitionDto;
import org.example.gatewayservice.repository.InMemoryRouteDefinitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.route.RouteDefinition;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceControllerTest {

    @Mock
    private InMemoryRouteDefinitionRepository routeRepository;

    @InjectMocks
    private ServiceController serviceController;

    private RouteDefinition enabledRoute;
    private RouteDefinition disabledRoute;
    private RouteDefinition defaultRoute;

    @BeforeEach
    void setUp() {
        enabledRoute = new RouteDefinition();
        enabledRoute.setId("enabled-service");
        enabledRoute.setUri(URI.create("http://enabled"));
        enabledRoute.setMetadata(Map.of("enabled", true));

        disabledRoute = new RouteDefinition();
        disabledRoute.setId("disabled-service");
        disabledRoute.setUri(URI.create("http://disabled"));
        disabledRoute.setMetadata(Map.of("enabled", false));

        defaultRoute = new RouteDefinition();
        defaultRoute.setId("default-service");
        defaultRoute.setUri(URI.create("http://default"));
        // No metadata, should default to true
    }

    @Test
    void getActiveServices_shouldReturnOnlyEnabledAndDefaultRoutes() {
        when(routeRepository.getRouteDefinitions()).thenReturn(Flux.just(enabledRoute, disabledRoute, defaultRoute));

        StepVerifier.create(serviceController.getActiveServices())
                .expectNextMatches(dto -> dto.routeId().equals("enabled-service") && Boolean.TRUE.equals(dto.enabled()))
                .expectNextMatches(dto -> dto.routeId().equals("default-service")) // default is considered enabled
                .verifyComplete();
    }

    @Test
    void getAllServices_shouldReturnAllRoutes() {
        when(routeRepository.getRouteDefinitions()).thenReturn(Flux.just(enabledRoute, disabledRoute, defaultRoute));

        StepVerifier.create(serviceController.getAllServices())
                .expectNextMatches(dto -> dto.routeId().equals("enabled-service"))
                .expectNextMatches(dto -> dto.routeId().equals("disabled-service"))
                .expectNextMatches(dto -> dto.routeId().equals("default-service"))
                .verifyComplete();
    }
}
