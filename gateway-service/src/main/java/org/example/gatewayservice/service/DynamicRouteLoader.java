package org.example.gatewayservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gatewayservice.repository.InMemoryRouteDefinitionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class DynamicRouteLoader {

    private final DynamicRouteService dynamicRouteService;
    private final RouteDefinitionWriter writer;
    private final InMemoryRouteDefinitionRepository memoryRepo;
    private final ApplicationEventPublisher publisher;

    public Mono<Void> refreshRoutes() {

        return dynamicRouteService.fetchActiveRoutes()
                .collectList()
                .flatMap(routes -> {

                    log.info("Fetched {} dynamic routes", routes.size());

                    // удаляем старые
                    return memoryRepo.getRouteDefinitions()
                            .flatMap(r -> writer.delete(Mono.just(r.getId()))
                                    .onErrorResume(err -> {
                                        log.warn("Delete failed: {}", err.toString());
                                        return Mono.empty();
                                    })
                            )
                            .thenMany(Flux.fromIterable(routes))
                            .flatMap(r -> writer.save(Mono.just(r)))
                            .then()
                            .doOnSuccess(v -> {
                                log.info("Publishing RefreshRoutesEvent...");
                                publisher.publishEvent(new RefreshRoutesEvent(this));
                            });
                });
    }
}