package org.example.dynamicroutingcore.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dynamicroutingcore.entity.RouteDefinitionEntity;
import org.example.dynamicroutingcore.repository.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteDefinitionService {

    private final RouteDefinitionRepository repository;
    private final ApplicationEventPublisher publisher;

    public List<RouteDefinitionEntity> getAllRoutes(){
        return repository.findAll();
    }

    public List<RouteDefinitionEntity> getActiveRoutes(){
        return repository.findAllByEnabledTrueOrderByRouteOrderAsc();
    }

    public RouteDefinitionEntity create(RouteDefinitionEntity route){
        RouteDefinitionEntity saved = repository.save(route);
//        refreshGateway();
        return saved;
    }

    public RouteDefinitionEntity update(UUID id, RouteDefinitionEntity route) {
        RouteDefinitionEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route does not exist " + id));

        existing.setRouteId(route.getRouteId());
        existing.setUri(route.getUri());
        existing.setPredicates(route.getPredicates());
        existing.setFilters(route.getFilters());
        existing.setRouteOrder(route.getRouteOrder());
        existing.setEnabled(route.getEnabled());


        RouteDefinitionEntity updated = repository.save(existing);
//        refreshGateway();
        return updated;
    }


    public void delete(UUID id){
        repository.deleteById(id);
//        refreshGateway();
    }

    public void toggleEnabled(UUID id, boolean enabled){
        RouteDefinitionEntity route = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route does not exist " + id));

        route.setEnabled(enabled);
        repository.save(route);

//        refreshGateway();
    }

//    public void refreshGateway(){
//        log.info("Publishing RefreshRoutesEvent....");
////        publisher.publishEvent(new RefreshRoutesEvent(this));
//    }

}
