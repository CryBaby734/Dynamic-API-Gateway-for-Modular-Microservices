package org.example.gatewayservice.dto;



import java.util.List;
import java.util.UUID;


public record RouteDefinitionDto(
        UUID id,
        String routeId,
        String uri,
        List<PredicateDto> predicates,
        List<FilterDto> filters,
        Integer routeOrder,
        Boolean enabled
) {}