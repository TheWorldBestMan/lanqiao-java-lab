package com.lanqiao.lab.submission;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.judge0")
public record Judge0Properties(
        boolean enabled,
        String baseUrl,
        int javaLanguageId,
        String authToken,
        int maxSourceBytes,
        int maxStdinBytes,
        int pollAttempts,
        long pollDelayMs) {
}

