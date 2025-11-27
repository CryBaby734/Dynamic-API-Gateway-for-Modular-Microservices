//package org.example.gatewayservice;
//
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class GatewayConfig {
//
//
//    @Bean
//    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
//        return builder.routes()
//                .route("auth_service", r -> r
//                        .path("/api/auth/**")
//                        .uri("http://localhost:8080")
//                )
//                .build();
//    }
//}
