package com.lanqiao.lab.problem;

import java.util.Arrays;
import java.util.List;

public final class ProblemDtos {
    private ProblemDtos() {}

    public record ProblemSummary(
            Long id, String slug, String title, String summary, Difficulty difficulty,
            String sourceType, String sourceLabel, String sourceRef,
            LicenseStatus licenseStatus, boolean contentAvailable, List<String> tags) {
        static ProblemSummary from(Problem p) {
            return new ProblemSummary(p.getId(), p.getSlug(), p.getTitle(), p.getSummary(), p.getDifficulty(),
                    p.getSourceType(), p.getSourceLabel(), p.getSourceRef(), p.getLicenseStatus(),
                    p.isContentAvailable(), splitTags(p.getTags()));
        }
    }

    public record ProblemDetail(
            Long id, String slug, String title, String summary, String descriptionMd, Difficulty difficulty,
            String sourceType, String sourceLabel, String sourceRef, LicenseStatus licenseStatus,
            boolean contentAvailable, int timeLimitMs, int memoryLimitMb, String templateCode,
            String sampleInput, String sampleOutput, List<String> tags) {
        static ProblemDetail from(Problem p) {
            return new ProblemDetail(p.getId(), p.getSlug(), p.getTitle(), p.getSummary(), p.getDescriptionMd(),
                    p.getDifficulty(), p.getSourceType(), p.getSourceLabel(), p.getSourceRef(), p.getLicenseStatus(),
                    p.isContentAvailable(), p.getTimeLimitMs(), p.getMemoryLimitMb(), p.getTemplateCode(),
                    p.getSampleInput(), p.getSampleOutput(), splitTags(p.getTags()));
        }
    }

    private static List<String> splitTags(String value) {
        if (value == null || value.isBlank()) return List.of();
        return Arrays.stream(value.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
    }
}

