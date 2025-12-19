package org.example.gatewayservice.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.core.Ordered;

import java.net.URI;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered{

    private final JwtTokenProvider jwtTokenProvider;
    private final BlacklistService blacklistService;

    @Override
    public int getOrder() {
        return -100;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        if (HttpMethod.OPTIONS.equals(exchange.getRequest().getMethod())) {
            return chain.filter(exchange);
        }

        // ORIGINAL PATH
        String path = exchange.getRequest().getURI().getPath();

        @SuppressWarnings("unchecked")
        Set<URI> originalUris =
                exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);

        if (originalUris != null && !originalUris.isEmpty()) {
            path = originalUris.iterator().next().getPath();
        }


        // 2. Разрешаем эндпоинты авторизации без токена
        if (path.startsWith("/api/auth/") || path.startsWith("/auth/")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!jwtTokenProvider.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        if (blacklistService.isBlacklisted(token)) {
            System.out.println("Token is blacklisted");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Claims claims = jwtTokenProvider.getClaims(token);
        String role = (String) claims.get("role");
        if(path.startsWith("/api/admin/")){
            if (!"ADMIN".equals(role)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        }
        System.out.println("✅ Authenticated user: " + claims.getSubject() + " | Role: " + claims.get("role"));
        return chain.filter(exchange);



    }
}