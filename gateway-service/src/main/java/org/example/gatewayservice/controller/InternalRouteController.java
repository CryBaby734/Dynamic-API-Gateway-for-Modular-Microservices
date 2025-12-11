package org.example.gatewayservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.gatewayservice.service.DynamicRouteLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/internal/routes")
@RequiredArgsConstructor
public class InternalRouteController {

    private final DynamicRouteLoader loader;

    @PostMapping("/refresh")
    public Mono<Void> refresh() {
        return loader.refreshRoutes();
    }


}