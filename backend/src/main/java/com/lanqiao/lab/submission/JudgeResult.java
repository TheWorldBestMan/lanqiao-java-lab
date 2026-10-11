package com.lanqiao.lab.submission;

public record JudgeResult(
        SubmissionStatus status,
        Integer runtimeMs,
        Integer memoryKb,
        String token,
        String message) {
}

