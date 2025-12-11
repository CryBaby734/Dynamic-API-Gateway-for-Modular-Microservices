package org.example.dynamicroutingcore.mapper;


import org.example.dynamicroutingcore.dto.RouteDefinitionDto;
import org.example.dynamicroutingcore.dto.RouteRequest;
import org.example.dynamicroutingcore.dto.RouteResponse;
import org.example.dynamicroutingcore.entity.FilterConfig;
import org.example.dynamicroutingcore.entity.PredicateConfig;
import org.example.dynamicroutingcore.entity.RouteDefinitionEntity;

import java.util.stream.Collectors;



public class RouteMapper {

    public static RouteDefinitionEntity toEntity(RouteRequest request) {
        return RouteDefinitionEntity.builder()
                .routeId(request.getRouteId())
                .uri(request.getUri())
                .predicates(request.getPredicates().stream()
                        .map(p -> new PredicateConfig(p.getName(), p.getArgs()))
                        .collect(Collectors.toList()))
                .filters(request.getFilters().stream()
                        .map(f -> new FilterConfig(f.getName(),f.getArgs()))
                        .collect(Collectors.toList()))
                .routeOrder(request.getRouteOrder())
                .enabled(request.getEnabled())
                .build();
    }

    public static RouteResponse toResponse(RouteDefinitionEntity entity) {
        return RouteResponse.builder()
                .id(entity.getId())
                .routeId(entity.getRouteId())
                .uri(entity.getUri())
                .routeOrder(entity.getRouteOrder())
                .enabled(entity.getEnabled())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .predicates(entity.getPredicates().stream()
                .map(p -> new RouteResponse.PredicateDto(p.getName(), p.getArgs()))
                .collect(Collectors.toList())
                )
                .filters(
                        entity.getFilters().stream()
                        .map(f -> new RouteResponse.FilterDto(f.getName(), f.getArgs()))
                        .collect(Collectors.toList())
                )
                        .build();

    }

    public static RouteDefinitionDto toDto(RouteDefinitionEntity entity) {
        return  new RouteDefinitionDto(
                entity.getId(),
                entity.getRouteId(),
                entity.getUri(),
                entity.getPredicates().stream()
                        .map(p -> new RouteDefinitionDto.PredicateDto(p.getName(), p.getArgs()))
                        .toList(),
                entity.getFilters().stream()
                        .map(f -> new RouteDefinitionDto.FilterDto(f.getName(), f.getArgs()))
                        .toList(),
                entity.getRouteOrder(),
                entity.getEnabled()
        );
    }


}
