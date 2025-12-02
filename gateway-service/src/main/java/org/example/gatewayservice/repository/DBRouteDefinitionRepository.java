//package org.example.gatewayservice.repository;
//
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.dynamicroutingcore.entity.RouteDefinitionEntity;
//import org.example.gatewayservice.converter.EntityToRouteDefinitionConverter;
//import org.springframework.cloud.gateway.route.RouteDefinition;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class DBRouteDefinitionRepository implements org.springframework.cloud.gateway.route.RouteDefinitionRepository{
//
//    private final org.example.dynamicroutingcore.repository.RouteDefinitionRepository jpaRouteRepo;
//    private final EntityToRouteDefinitionConverter converter;
//
//    @Override
//    public Flux<RouteDefinition> getRouteDefinitions() {
//        log.info("Loading dynamic routes from PostgreSQL...");
//
//        List<RouteDefinitionEntity> routes = jpaRouteRepo.findAllByEnabledTrueOrderByRouteOrderAsc();
//        return Flux.fromIterable(
//                routes.stream()
//                        .map(converter::convert)
//                        .toList()
//        );
//    }
//
//
//    @Override
//    public Mono<Void> save(Mono<RouteDefinition> route) {
//        return Mono.empty();
//    }
//
//
//    @Override
//    public Mono<Void> delete(Mono<String> routeId) {
//        return Mono.empty();
//    }
//}
