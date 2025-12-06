package org.example.gatewayservice.config;


import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public RedisRateLimiter defaultRateLimiter() {
        // 5 запросов в секунду, burst = 10
        return new RedisRateLimiter(5, 10);
    }
}