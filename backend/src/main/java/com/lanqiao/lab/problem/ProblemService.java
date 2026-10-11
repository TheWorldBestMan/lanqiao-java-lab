package com.lanqiao.lab.problem;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

import static com.lanqiao.lab.problem.ProblemDtos.ProblemDetail;
import static com.lanqiao.lab.problem.ProblemDtos.ProblemSummary;

@Service
public class ProblemService {
    private final ProblemRepository repository;

    public ProblemService(ProblemRepository repository) {
        this.repository = repository;
    }

    public List<ProblemSummary> list(String keyword, Difficulty difficulty, String sourceType) {
        String q = keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT);
        return repository.findAll().stream()
                .filter(p -> q.isEmpty() || p.getTitle().toLowerCase(Locale.ROOT).contains(q)
                        || p.getTags().toLowerCase(Locale.ROOT).contains(q))
                .filter(p -> difficulty == null || p.getDifficulty() == difficulty)
                .filter(p -> sourceType == null || sourceType.isBlank() || p.getSourceType().equalsIgnoreCase(sourceType))
                .map(ProblemSummary::from)
                .toList();
    }

    public ProblemDetail get(String slug) {
        return ProblemDetail.from(requireBySlug(slug));
    }

    public Problem requireById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProblemNotFoundException("题目不存在"));
    }

    private Problem requireBySlug(String slug) {
        return repository.findBySlug(slug).orElseThrow(() -> new ProblemNotFoundException("题目不存在"));
    }
}

