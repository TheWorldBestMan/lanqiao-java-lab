package com.lanqiao.lab.submission;

import com.lanqiao.lab.problem.Problem;
import com.lanqiao.lab.problem.ProblemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class SubmissionService {
    private final SubmissionRepository repository;
    private final ProblemService problems;
    private final JudgeGateway judgeGateway;
    private final Judge0Properties properties;
    private final SubmissionRateLimiter rateLimiter;

    public SubmissionService(SubmissionRepository repository, ProblemService problems, JudgeGateway judgeGateway,
                             Judge0Properties properties, SubmissionRateLimiter rateLimiter) {
        this.repository = repository;
        this.problems = problems;
        this.judgeGateway = judgeGateway;
        this.properties = properties;
        this.rateLimiter = rateLimiter;
    }

    @Transactional
    public SubmissionView create(CreateSubmission request, String userKey) {
        if (!rateLimiter.allow(userKey)) throw new IllegalArgumentException("提交过于频繁，请一分钟后再试");
        int bytes = request.sourceCode().getBytes(StandardCharsets.UTF_8).length;
        if (bytes > properties.maxSourceBytes()) throw new IllegalArgumentException("源码超过大小限制");
        Problem problem = problems.requireById(request.problemId());
        if (!problem.isContentAvailable()) throw new IllegalArgumentException("该题尚未取得内容授权，不能提交");

        Submission submission = repository.save(new Submission(problem, userKey, request.sourceCode()));
        submission.complete(judgeGateway.judge(problem, request.sourceCode()));
        return SubmissionView.from(repository.save(submission));
    }

    @Transactional(readOnly = true)
    public SubmissionView get(String id) {
        return repository.findById(id).map(SubmissionView::from)
                .orElseThrow(() -> new IllegalArgumentException("提交记录不存在"));
    }

    @Transactional(readOnly = true)
    public List<SubmissionView> recent(String userKey) {
        return repository.findTop20ByUserKeyOrderByCreatedAtDesc(userKey).stream().map(SubmissionView::from).toList();
    }
}

