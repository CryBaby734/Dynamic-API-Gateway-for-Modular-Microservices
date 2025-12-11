package org.example.gatewayservice.converter;

import org.example.gatewayservice.dto.FilterDto;
import org.example.gatewayservice.dto.PredicateDto;
import org.example.gatewayservice.dto.RouteDefinitionDto;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class RouteDtoToSpringConverter {

    public RouteDefinition convert(RouteDefinitionDto dto) {

        RouteDefinition rd = new RouteDefinition();

        rd.setId(dto.routeId());
        rd.setUri(URI.create(dto.uri()));
        rd.setOrder(dto.routeOrder() == null ? 0 : dto.routeOrder());

        rd.setPredicates(
                dto.predicates().stream()
                        .map(this::convertPredicate)
                        .toList()
        );

        rd.setFilters(
                dto.filters().stream()
                        .map(this::convertFilter)
                        .toList()
        );

        return rd;
    }

    private PredicateDefinition convertPredicate(PredicateDto dto) {
        PredicateDefinition pd = new PredicateDefinition();
        pd.setName(dto.name());
        pd.setArgs(dto.args());
        return pd;
    }

    private FilterDefinition convertFilter(FilterDto dto) {
        FilterDefinition fd = new FilterDefinition();
        fd.setName(dto.name());
        fd.setArgs(dto.args());
        return fd;
    }
}