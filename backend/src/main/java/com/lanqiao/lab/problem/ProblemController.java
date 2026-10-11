package com.lanqiao.lab.problem;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lanqiao.lab.problem.ProblemDtos.ProblemDetail;
import static com.lanqiao.lab.problem.ProblemDtos.ProblemSummary;

@RestController
@RequestMapping("/api/v1/problems")
public class ProblemController {
    private final ProblemService service;

    public ProblemController(ProblemService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProblemSummary> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) String sourceType) {
        return service.list(keyword, difficulty, sourceType);
    }

    @GetMapping("/{slug}")
    public ProblemDetail get(@PathVariable String slug) {
        return service.get(slug);
    }
}

