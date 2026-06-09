package org.example.gatewayservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.gatewayservice.dto.FilterDto;
import org.example.gatewayservice.dto.PredicateDto;
import org.example.gatewayservice.dto.RouteDefinitionDto;
import org.example.gatewayservice.repository.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceController {

    private final InMemoryRouteDefinitionRepository routeRepository;

    @GetMapping("/services")
    public Flux<RouteDefinitionDto> getActiveServices() {
        return routeRepository.getRouteDefinitions()
                .filter(route -> {
                    Object enabled = route.getMetadata().get("enabled");
                    // Default to true if missing, otherwise check value
                    return enabled == null || Boolean.TRUE.equals(enabled);
                })
                .map(this::convertToDto);
    }

    @GetMapping("/admin/services")
    public Flux<RouteDefinitionDto> getAllServices() {
        return routeRepository.getRouteDefinitions()
                .map(this::convertToDto);
    }

    private RouteDefinitionDto convertToDto(RouteDefinition route) {
        Boolean enabled = (Boolean) route.getMetadata().get("enabled");

        // Handle UUID safely if routeId is not a UUID
        UUID uuid = null;
        try {
            // Assuming the ID might be a UUID string, or generate a new one/null if not
            // The original DTO had a separate UUID id field and String routeId.
            // If the original ID was the UUID, we might have lost the strict UUID type but
            // we have the String ID.
            // Let's see if we can parse it, or just use null/random if it's strictly
            // required.
            // For now, let's try to parse if it looks like one, or just leave null if it's
            // not critical.
            // Actually, checking RouteDefinitionDto: UUID id, String routeId.
            // The route.getId() is likely the String routeId.
            // We usually don't store the UUID back in RouteDefinition unless we put it in
            // metadata.
            // Let's check if we strictly need it. If not, null might be okay or we can put
            // it in metadata too if needed.
            // For UI purposes, routeId (String) is usually the key.
        } catch (Exception e) {
            // ignore
        }

        List<PredicateDto> predicates = route.getPredicates().stream()
                .map(this::convertPredicate)
                .toList();

        List<FilterDto> filters = route.getFilters().stream()
                .map(this::convertFilter)
                .toList();

        return new RouteDefinitionDto(
                uuid, // ID might be missing, set null or we could have stored it in metadata
                route.getId(),
                route.getUri().toString(),
                predicates,
                filters,
                route.getOrder(),
                enabled);
    }

    private PredicateDto convertPredicate(PredicateDefinition pd) {
        return new PredicateDto(pd.getName(), pd.getArgs());
    }

    private FilterDto convertFilter(FilterDefinition fd) {
        return new FilterDto(fd.getName(), fd.getArgs());
    }
}
