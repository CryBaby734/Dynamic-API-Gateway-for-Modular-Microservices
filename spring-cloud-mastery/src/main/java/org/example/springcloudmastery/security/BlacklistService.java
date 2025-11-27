package org.example.springcloudmastery.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    public void blacklistToken(String token, long ttlSeconds) {
        redisTemplate.opsForValue().set(token, "blacklisted", ttlSeconds, TimeUnit.SECONDS);
    }
}