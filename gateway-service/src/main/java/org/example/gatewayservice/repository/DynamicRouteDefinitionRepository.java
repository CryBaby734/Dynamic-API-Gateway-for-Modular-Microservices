package org.example.gatewayservice.repository;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gatewayservice.service.DynamicRouteService;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class DynamicRouteDefinitionRepository implements RouteDefinitionRepository {

    private final DynamicRouteService dynamicRouteService;


    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        log.info("Loading route definitions from dynamic-routing-core (WebClient)...");
        return dynamicRouteService.fetchActiveRoutes();
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return Mono.error(new UnsupportedOperationException("Dynamic routes are managed by dynamic-routing-core"));
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return Mono.error(new UnsupportedOperationException("Dynamic routes are managed by dynamic-routing-core"));
    }
}
