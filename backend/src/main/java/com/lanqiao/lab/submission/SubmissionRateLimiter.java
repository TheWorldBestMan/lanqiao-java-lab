package com.lanqiao.lab.submission;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class SubmissionRateLimiter {
    private final StringRedisTemplate redis;
    private final boolean enabled;

    public SubmissionRateLimiter(StringRedisTemplate redis,
                                 @Value("${app.redis.rate-limit-enabled:false}") boolean enabled) {
        this.redis = redis;
        this.enabled = enabled;
    }

    public boolean allow(String userKey) {
        if (!enabled) return true;
        String key = "submit-rate:" + userKey;
        Long count = redis.opsForValue().increment(key);
        if (count != null && count == 1) redis.expire(key, Duration.ofMinutes(1));
        return count != null && count <= 10;
    }
}

