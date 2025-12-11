package org.example.dynamicroutingcore.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.dynamicroutingcore.dto.RouteRequest;
import org.example.dynamicroutingcore.entity.RouteDefinitionEntity;
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
    private final ApplicationEventPublisher publisher;
    private final RouteValidator routeValidator;

    public List<RouteDefinitionEntity> getAllRoutes(){
        return repository.findAll();
    }

    public List<RouteDefinitionEntity> getActiveRoutes(){
        return repository.findAllByEnabledTrueOrderByRouteOrderAsc();
    }

    public RouteDefinitionEntity create(RouteRequest request){
        routeValidator.validate(request);
        RouteDefinitionEntity entity = RouteMapper.toEntity(request);

        return repository.save(entity);
    }

    public RouteDefinitionEntity update(UUID id, RouteRequest request) {
        RouteDefinitionEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route does not exist " + id));

        routeValidator.validate(request);

        RouteDefinitionEntity updated = RouteMapper.toEntity(request);
        updated.setId(id);
        return repository.save(updated);
    }


    public void delete(UUID id){
        repository.deleteById(id);
//        refreshGateway();
    }

    public void toggleEnabled(UUID id, boolean enabled) {
        RouteDefinitionEntity route = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route does not exist " + id));

        route.setEnabled(enabled);
        repository.save(route);

    }
//        public RouteDefinition toSpringRouteDefinition(RouteDefinitionDto dto) {
//            RouteDefinition rd = new RouteDefinition();
//            rd.setId(dto.routeId());
//            rd.setUri(URI.create(dto.uri()));
//            rd.setOrder(dto.routeOrder() == null ? 0 : dto.routeOrder());
////Predicates
//            List<org.springframework.cloud.gateway.handler.predicate.PredicateDefinition> predicateDefs =
//                    dto.predicates().stream()
//                            .map(p -> {
//                                var pd = new org.springframework.cloud.gateway.handler.predicate.PredicateDefinition();
//                                pd.setName(p.getName());
//                                pd.setArgs(p.getArgs());
//                                return pd;
//                            })
//                            .toList();
//
//            rd.setPredicates(predicateDefs);
//
//
//            List<org.springframework.cloud.gateway.filter.FilterDefinition> filterDefs =
//                    dto.filters().stream()
//                            .map(f -> {
//                                var fd = new org.springframework.cloud.gateway.filter.FilterDefinition();
//                                fd.setName(f.getName());
//                                fd.setArgs(f.getArgs());
//                                return fd;
//                            }).toList();
//
//            rd.setFilters(filterDefs);
//            return rd;
//        }
//
//
//        public List<RouteDefinition> getSpringRouteDefinitions() {
//        return getActiveRoutes().stream()
//                .map(RouteMapper::toDto)
//                .map(this::toSpringRouteDefinition)
//                .toList();
//        }






//        refreshGateway();


//    public void refreshGateway(){
//        log.info("Publishing RefreshRoutesEvent....");
////        publisher.publishEvent(new RefreshRoutesEvent(this));
//    }

}
