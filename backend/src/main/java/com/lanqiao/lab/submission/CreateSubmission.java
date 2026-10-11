package com.lanqiao.lab.submission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateSubmission(
        @NotNull Long problemId,
        @NotBlank @Size(max = 50000) String sourceCode) {
}

