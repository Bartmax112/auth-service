package com.wspa.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
class AuthRedisTemplate {
    private final StringRedisTemplate redisTemplate;

    void put(String key, Object value, int expireDuration, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, asJsonString(value));
        redisTemplate.expire(key, expireDuration, timeUnit);
    }

    Boolean delete(String key) {
        return redisTemplate.opsForValue().getOperations().delete(key);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
