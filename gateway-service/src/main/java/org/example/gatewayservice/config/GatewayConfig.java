//package org.example.gatewayservice.config;
//
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Configuration
//public class GatewayConfig {
//
//    @Bean
//    @LoadBalanced
//    public WebClient routeWebClient(WebClient.Builder builder) {
//        // Если dynamic-routing-core регистрируется в Eureka как "dynamic-routing-core"
//        return builder
//                .baseUrl("http://dynamic-routing-core")
//                .build();
//    }
//}