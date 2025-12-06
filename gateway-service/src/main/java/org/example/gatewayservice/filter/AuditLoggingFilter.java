package org.example.gatewayservice.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Slf4j
@Component
public class AuditLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        long startTime = System.currentTimeMillis();

        var request = exchange.getRequest();
        String method = request.getMethod() != null
                ? request.getMethod().name()
                : "UNKNOWN";
        String path = request.getPath().value();

        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        String routeId = route != null ? route.getId() : "UNKNOWN_ROUTE";
        String targetUri  = route != null ? route.getUri().toString() : "UNKNOWN_URI";

        log.info("[AUDIT][REQUEST] method={} path={} routeId={} targetUri={}",
                method, path, routeId, targetUri);

        return chain.filter(exchange)
                // ошибки
                .doOnError(ex -> {
                    long duration = System.currentTimeMillis() - startTime;
                    int statusCode = exchange.getResponse().getStatusCode() != null
                            ? exchange.getResponse().getStatusCode().value()
                            : 0;

                    log.error("[AUDIT][ERROR] method={} path={} routeId={} status={} durationMs={} error={}",
                            method, path, routeId, statusCode, duration, ex.getMessage(), ex);
                })
                // успех или завершение
                .doOnTerminate(() -> {
                    long duration = System.currentTimeMillis() - startTime;
                    int statusCode = exchange.getResponse().getStatusCode() != null
                            ? exchange.getResponse().getStatusCode().value()
                            : 0;

                    log.info("[AUDIT][RESPONSE] method={} path={} routeId={} status={} durationMs={}",
                            method, path, routeId, statusCode, duration);
                });
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
