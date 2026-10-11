package com.lanqiao.lab.learning;

import com.lanqiao.lab.problem.ProblemRepository;
import com.lanqiao.lab.submission.SubmissionRepository;
import com.lanqiao.lab.submission.SubmissionStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/learning")
public class LearningController {
    private final ProblemRepository problems;
    private final SubmissionRepository submissions;

    public LearningController(ProblemRepository problems, SubmissionRepository submissions) {
        this.problems = problems;
        this.submissions = submissions;
    }

    @GetMapping("/knowledge-tree")
    public List<KnowledgeStage> knowledgeTree() {
        return List.of(
                new KnowledgeStage("foundation", "Java 基础", "先把输入输出、分支与循环练到熟练", 72, List.of(
                        new KnowledgeNode("io", "快速输入输出", "MASTERED", 8, 8),
                        new KnowledgeNode("flow", "分支与循环", "LEARNING", 6, 9),
                        new KnowledgeNode("array", "数组与字符串", "LEARNING", 4, 10))),
                new KnowledgeStage("core-algorithm", "核心算法", "蓝桥杯常用模型，强调识别与模板化", 38, List.of(
                        new KnowledgeNode("sort", "排序与查找", "LEARNING", 3, 8),
                        new KnowledgeNode("prefix", "前缀和与差分", "READY", 1, 8),
                        new KnowledgeNode("two-pointers", "双指针与滑窗", "READY", 1, 7))),
                new KnowledgeStage("advanced", "进阶冲刺", "图论、动态规划与综合模拟", 12, List.of(
                        new KnowledgeNode("search", "DFS / BFS", "LOCKED", 0, 10),
                        new KnowledgeNode("dp", "动态规划", "LOCKED", 0, 12),
                        new KnowledgeNode("graph", "最短路与并查集", "LOCKED", 0, 9)))
        );
    }

    @GetMapping("/overview")
    public LearningOverview overview() {
        var recent = submissions.findTop20ByUserKeyOrderByCreatedAtDesc("demo-user");
        long accepted = recent.stream().filter(s -> s.getStatus() == SubmissionStatus.ACCEPTED).count();
        return new LearningOverview(12, 4, accepted, problems.count(), 5, 68,
                List.of(62, 74, 58, 46, 35), List.of("基础语法", "数组", "模拟", "滑动窗口", "搜索"));
    }

    @GetMapping("/wrong-problems")
    public List<WrongProblem> wrongProblems() {
        return submissions.findTop20ByUserKeyOrderByCreatedAtDesc("demo-user").stream()
                .filter(s -> s.getStatus() != SubmissionStatus.ACCEPTED && s.getStatus() != SubmissionStatus.JUDGE_UNAVAILABLE)
                .map(s -> new WrongProblem(s.getProblem().getSlug(), s.getProblem().getTitle(),
                        s.getProblem().getDifficulty().name(), s.getStatus().name(), s.getCreatedAt().toString()))
                .distinct().toList();
    }

    public record KnowledgeStage(String id, String title, String description, int progress, List<KnowledgeNode> nodes) {}
    public record KnowledgeNode(String id, String title, String state, int completed, int total) {}
    public record LearningOverview(int studyDays, int currentStreak, long acceptedToday, long availableProblems,
                                   int weeklyGoal, int overallProgress, List<Integer> mastery,
                                   List<String> masteryLabels) {}
    public record WrongProblem(String slug, String title, String difficulty, String lastStatus, String updatedAt) {}
}

