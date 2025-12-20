package org.example.gatewayservice.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class MetricsGlobalFilter implements GlobalFilter, Ordered {

    private final MeterRegistry meterRegistry;

    public MetricsGlobalFilter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Timer.Sample sample = Timer.start(meterRegistry);

        return chain.filter(exchange)
                .doFinally(signalType -> {

                    Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
                    String routeId = route != null ? route.getId() : "unknown";

                    String method = exchange.getRequest().getMethod() != null
                            ? exchange.getRequest().getMethod().name()
                            : "UNKNOWN";

                    HttpStatusCode status = exchange.getResponse().getStatusCode();
                    int statusCode = status != null ? status.value() : 500;

                    // ALL requests
                    Counter.builder("gateway_requests_total")
                            .tag("routeId", routeId)
                            .tag("method", method)
                            .tag("status", String.valueOf(statusCode))
                            .register(meterRegistry)
                            .increment();

                    // ERRORS (4xx + 5xx)
                    if (statusCode >= 400) {
                        Counter.builder("gateway_error_total")
                                .tag("routeId", routeId)
                                .tag("status", String.valueOf(statusCode))
                                .register(meterRegistry)
                                .increment();
                    }

                    // LATENCY (Histogram + Percentiles)
                    sample.stop(
                            Timer.builder("gateway_request_duration_seconds")
                                    .publishPercentileHistogram(true)
                                    .publishPercentiles(0.5, 0.85, 0.95)
                                    .tag("routeId", routeId)
                                    .tag("method", method)
                                    .register(meterRegistry)
                    );
                });
    }

    @Override
    public int getOrder() {
        return -1; // раньше большинства фильтров
    }
}