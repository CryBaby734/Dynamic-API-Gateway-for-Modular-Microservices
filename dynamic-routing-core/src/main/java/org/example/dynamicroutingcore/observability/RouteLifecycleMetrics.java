package org.example.dynamicroutingcore.observability;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteLifecycleMetrics {

    private final MeterRegistry meterRegistry;


    public void increment(String event, String routeId) {
        Counter.builder("route_lifecycle_events_total")
                .tag("event", event)
                .tag("routeId", routeId)
                .register(meterRegistry)
                .increment();
    }

}
