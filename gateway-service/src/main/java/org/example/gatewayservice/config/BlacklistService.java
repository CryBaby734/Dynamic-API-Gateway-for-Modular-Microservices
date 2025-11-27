package org.example.gatewayservice.config;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class BlacklistService {

    private final StringRedisTemplate redisTemplate;


    public void blacklistToken(String token, long expirationSeconds) {
        redisTemplate.opsForValue().set(
                token,
                "blacklisted",
                Duration.ofSeconds(expirationSeconds)
        );
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }

}
