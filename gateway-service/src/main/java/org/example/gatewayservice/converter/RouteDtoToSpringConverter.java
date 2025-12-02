package org.example.gatewayservice.converter;

import org.example.gatewayservice.dto.FilterDto;
import org.example.gatewayservice.dto.PredicateDto;
import org.example.gatewayservice.dto.RouteDefinitionResponse;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Map;
@Component
public class RouteDtoToSpringConverter {

    public RouteDefinition convert(RouteDefinitionResponse dto) {
        RouteDefinition rd = new RouteDefinition();

        rd.setId(dto.getId());
        rd.setUri(URI.create(dto.getUri()));
        rd.setOrder(dto.getOrder());


        List<PredicateDefinition> springPredicates = dto.getPredicates().stream()
                .map(this::convertPredicate)
                .toList();
        rd.setPredicates(springPredicates);

        List<FilterDefinition> springFilters = dto.getFilters().stream()
                .map(this::convertFilter)
                .toList();
        rd.setFilters(springFilters);

        return rd;
    }

    private PredicateDefinition convertPredicate(PredicateDto dto) {
        PredicateDefinition pd = new PredicateDefinition();
        pd.setName(dto.getName());
        pd.setArgs(dto.getArgs());
        return pd;
    }

    private FilterDefinition convertFilter(FilterDto dto) {
        FilterDefinition fd = new FilterDefinition();
        fd.setName(dto.getName());
        fd.setArgs(dto.getArgs());
        return fd;
    }

}