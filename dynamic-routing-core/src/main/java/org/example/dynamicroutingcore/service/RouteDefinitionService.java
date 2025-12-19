package org.example.dynamicroutingcore.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.dynamicroutingcore.dto.RouteRequest;
import org.example.dynamicroutingcore.entity.RouteDefinitionEntity;
import org.example.dynamicroutingcore.event.RouteChangedEvent;
import org.example.dynamicroutingcore.event.RouteEventType;
import org.example.dynamicroutingcore.mapper.RouteMapper;
import org.example.dynamicroutingcore.repository.RouteDefinitionRepository;
import org.example.dynamicroutingcore.validator.RouteValidator;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteDefinitionService {


    private final RouteDefinitionRepository repository;
    private final RouteValidator routeValidator;
    private final RouteEventProducer routeEventProducer;

    public List<RouteDefinitionEntity> getAllRoutes() {
        return repository.findAll();
    }

    public List<RouteDefinitionEntity> getActiveRoutes() {
        return repository.findAllByEnabledTrueOrderByRouteOrderAsc();
    }

    public RouteDefinitionEntity create(RouteRequest request) {
        routeValidator.validate(request);

        RouteDefinitionEntity saved =
                repository.save(RouteMapper.toEntity(request));

        publish(RouteEventType.ROUTE_CREATED, saved);
        return saved;
    }

    public RouteDefinitionEntity update(UUID id, RouteRequest request) {
        RouteDefinitionEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route does not exist " + id));

        routeValidator.validate(request);
        existing.updateFrom(request);

        RouteDefinitionEntity saved = repository.save(existing);
        publish(RouteEventType.ROUTE_UPDATED, saved);

        return saved;
    }

    public void delete(UUID id) {
        RouteDefinitionEntity route = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route does not exist " + id));

        repository.delete(route);
        routeEventProducer.publish(
                RouteChangedEvent.builder()
                        .eventType(RouteEventType.ROUTE_DELETED)
                        .routeId(route.getRouteId().toString())
                        .build()
        );
    }

    public void toggleEnabled(UUID id, boolean enabled) {
        RouteDefinitionEntity route = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route does not exist " + id));

        route.setEnabled(enabled);
        repository.save(route);

        routeEventProducer.publish(
                RouteChangedEvent.builder()
                        .eventType(enabled
                                ? RouteEventType.ROUTE_ENABLED
                                : RouteEventType.ROUTE_DISABLED)
                        .routeId(route.getRouteId().toString())
                        .build()
        );
    }

    private void publish(RouteEventType type, RouteDefinitionEntity route) {
        routeEventProducer.publish(
                RouteChangedEvent.builder()
                        .eventType(type)
                        .routeId(route.getRouteId().toString())
                        .build()
        );
    }
}
