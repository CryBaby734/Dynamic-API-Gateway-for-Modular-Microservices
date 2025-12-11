package org.example.gatewayservice.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;

import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Primary
@Component
public class InMemoryRouteDefinitionRepository implements RouteDefinitionWriter {

    // Хранилище маршрутов в памяти gateway
    private final Map<String, RouteDefinition> routes = new ConcurrentHashMap<>();


    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routes.values());
    }


    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.doOnNext(r -> {
            log.info("➕ Saving route in memory: {}", r.getId());
            routes.put(r.getId(), r);
        }).then();
    }


    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.doOnNext(id -> {
            log.info("🗑 Removing route from memory: {}", id);
            routes.remove(id);
        }).then();
    }


    // Удобный метод очистки всех маршрутов
    public Mono<Void> clearAll() {
        log.info("🧹 Clearing all in-memory routes...");
        routes.clear();
        return Mono.empty();
    }
}