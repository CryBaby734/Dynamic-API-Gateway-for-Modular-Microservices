package org.example.gatewayservice;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/users")
    public Mono<ResponseEntity<List<Map<String, Object>>>> users() {
        Map<String, Object> item = Map.of(
                "service", "user-service",
                "status", "unavailable",
                "timestamp", Instant.now().toString()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(List.of(item)));
    }

    @GetMapping("/auth")
    public Mono<ResponseEntity<Map<String, Object>>> auth() {
        Map<String, Object> body = Map.of(
                "service", "auth-service",
                "status", "unavailable",
                "timestamp", Instant.now().toString()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body));
    }
}