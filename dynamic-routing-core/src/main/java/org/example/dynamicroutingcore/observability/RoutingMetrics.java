package org.example.dynamicroutingcore.observability;


import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.dynamicroutingcore.service.RouteDefinitionService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoutingMetrics {

    private final MeterRegistry meterRegistry;
    private final RouteDefinitionService routeDefinitionService;


    @PostConstruct
    void registerGauges() {
        Gauge.builder("routes_active_count", routeDefinitionService, RouteDefinitionService::countActiveRoutes)
                .description("Number of active (enabled) routes")
                .register(meterRegistry);

        Gauge.builder("routes_disabled_count", routeDefinitionService, RouteDefinitionService::countDisabledRoutes)
                .description("Number of disabled routes")
                .register(meterRegistry);
    }
}
