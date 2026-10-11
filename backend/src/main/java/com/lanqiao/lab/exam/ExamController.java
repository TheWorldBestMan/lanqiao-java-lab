package com.lanqiao.lab.exam;

import com.lanqiao.lab.problem.ProblemRepository;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exams")
public class ExamController {
    private final ProblemRepository problems;

    public ExamController(ProblemRepository problems) {
        this.problems = problems;
    }

    @GetMapping
    public List<ExamSummary> list() {
        return List.of(
                new ExamSummary("weekly-basic", "基础能力周测", 60, 3, "AVAILABLE", "原创模拟题，不对应任何真实赛事"),
                new ExamSummary("lanqiao-format", "蓝桥杯赛制适应卷", 120, 6, "COMING_SOON", "结构演示；不含未授权真题"));
    }

    @PostMapping("/{examId}/sessions")
    public ExamSession start(@PathVariable String examId) {
        if (!"weekly-basic".equals(examId)) throw new IllegalArgumentException("该试卷尚未开放");
        var selected = problems.findAll().stream().filter(p -> p.isContentAvailable()).limit(3)
                .map(p -> new ExamProblem(p.getId(), p.getSlug(), p.getTitle(), p.getDifficulty().name())).toList();
        return new ExamSession(UUID.randomUUID().toString(), examId, Instant.now(), 60, selected);
    }

    public record ExamSummary(String id, String title, int durationMinutes, int problemCount, String status, String note) {}
    public record ExamProblem(Long id, String slug, String title, String difficulty) {}
    public record ExamSession(String id, String examId, Instant startedAt, int durationMinutes, List<ExamProblem> problems) {}
}

