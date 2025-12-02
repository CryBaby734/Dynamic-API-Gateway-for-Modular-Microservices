//package org.example.gatewayservice.converter;
//
//import lombok.extern.slf4j.Slf4j;
//import org.example.dynamicroutingcore.entity.FilterDefinition;
//import org.example.dynamicroutingcore.entity.PredicateDefinition;
//import org.example.dynamicroutingcore.entity.RouteDefinitionEntity;
//import org.springframework.cloud.gateway.route.RouteDefinition;
//import org.springframework.stereotype.Component;
//
//import java.net.URI;
//
//@Component
//@Slf4j
//public class EntityToRouteDefinitionConverter {
//
//    public RouteDefinition convert(RouteDefinitionEntity entity) {
//
//        RouteDefinition rd = new RouteDefinition();
//
//        // Set basic fields
//        rd.setId(entity.getRouteId());
//        rd.setUri(URI.create(entity.getUri()));
//
//        // Convert predicates
//        rd.setPredicates(
//                entity.getPredicates().stream()
//                        .map(p -> new org.springframework.cloud.gateway.handler.predicate.PredicateDefinition(
//                                buildPredicateString(p)
//                        ))
//                        .toList()
//        );
//
//        // Convert filters
//        rd.setFilters(
//                entity.getFilters().stream()
//                        .map(f -> new org.springframework.cloud.gateway.filter.FilterDefinition(
//                                buildFilterString(f)
//                        ))
//                        .toList()
//        );
//
//        return rd;
//    }
//
//    // Example: "Path=/api/users/**"
//    private String buildPredicateString(PredicateDefinition p) {
//        // Берём первое значение из карты аргументов
//        String argValue = p.getArgs().values().stream().findFirst().orElse("");
//        return p.getName() + "=" + argValue;
//    }
//
//    // Example: "StripPrefix=1"
//    private String buildFilterString(FilterDefinition f) {
//        String argValue = f.getArgs().values().stream().findFirst().orElse("");
//        return f.getName() + "=" + argValue;
//    }
//}