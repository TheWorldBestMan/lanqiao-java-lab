package com.lanqiao.lab.submission;

import com.lanqiao.lab.problem.Problem;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column(name = "user_key", nullable = false, length = 80)
    private String userKey;

    @Lob
    @Column(name = "source_code", nullable = false)
    private String sourceCode;

    @Column(nullable = false, length = 24)
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private SubmissionStatus status;

    private Integer score;

    @Column(name = "runtime_ms")
    private Integer runtimeMs;

    @Column(name = "memory_kb")
    private Integer memoryKb;

    @Column(name = "judge_token", length = 120)
    private String judgeToken;

    @Column(length = 500)
    private String message;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Submission() {}

    public Submission(Problem problem, String userKey, String sourceCode) {
        this.id = UUID.randomUUID().toString();
        this.problem = problem;
        this.userKey = userKey;
        this.sourceCode = sourceCode;
        this.language = "JAVA";
        this.status = SubmissionStatus.QUEUED;
        this.createdAt = Instant.now();
    }

    public void complete(JudgeResult result) {
        this.status = result.status();
        this.score = result.status() == SubmissionStatus.ACCEPTED ? 100 : 0;
        this.runtimeMs = result.runtimeMs();
        this.memoryKb = result.memoryKb();
        this.judgeToken = result.token();
        this.message = result.message();
    }

    public String getId() { return id; }
    public Problem getProblem() { return problem; }
    public String getUserKey() { return userKey; }
    public String getSourceCode() { return sourceCode; }
    public String getLanguage() { return language; }
    public SubmissionStatus getStatus() { return status; }
    public Integer getScore() { return score; }
    public Integer getRuntimeMs() { return runtimeMs; }
    public Integer getMemoryKb() { return memoryKb; }
    public String getJudgeToken() { return judgeToken; }
    public String getMessage() { return message; }
    public Instant getCreatedAt() { return createdAt; }
}

