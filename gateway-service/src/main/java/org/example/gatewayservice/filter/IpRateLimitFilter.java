package org.example.gatewayservice.filter;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpRateLimitFilter implements GlobalFilter, Ordered {

    private final RedisRateLimiter redisRateLimiter;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String ip = exchange.getRequest().getRemoteAddress() != null ?
                exchange.getRequest().getRemoteAddress().getAddress().getHostAddress() :
                "UNKNOWN_IP";

        return redisRateLimiter.isAllowed("GLOBAL_RATE_LIMIT", ip)
                .flatMap(response -> {
                    if (response.isAllowed()) {
                        return chain.filter(exchange);
                    } else {
                        log.warn("[RATE_LIMIT][BLOCKED] ip={} reason=too_many_requests", ip);
                        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                        return exchange.getResponse().setComplete();
                    }
                });
    }
    @Override
    public int getOrder(){
        return -1;
    }
}
