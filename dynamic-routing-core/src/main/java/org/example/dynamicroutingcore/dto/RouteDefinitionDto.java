package org.example.dynamicroutingcore.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record RouteDefinitionDto(
        UUID id,
        String routeId,
        String uri,
        List<PredicateDto> predicates,
        List<FilterDto> filters,
        Integer routeOrder,
        Boolean enabled
) {

    public record PredicateDto(
            String name,
            Map<String, String> args
    ) {}

    public record FilterDto(
            String name,
            Map<String, String> args
    ) {}
}