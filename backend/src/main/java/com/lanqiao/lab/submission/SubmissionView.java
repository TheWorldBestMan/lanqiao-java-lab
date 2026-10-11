package com.lanqiao.lab.submission;

import java.time.Instant;

public record SubmissionView(
        String id, Long problemId, String problemTitle, SubmissionStatus status,
        Integer score, Integer runtimeMs, Integer memoryKb, String message, Instant createdAt) {
    static SubmissionView from(Submission s) {
        return new SubmissionView(s.getId(), s.getProblem().getId(), s.getProblem().getTitle(), s.getStatus(),
                s.getScore(), s.getRuntimeMs(), s.getMemoryKb(), s.getMessage(), s.getCreatedAt());
    }
}

