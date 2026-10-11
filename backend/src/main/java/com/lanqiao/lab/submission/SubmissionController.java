package com.lanqiao.lab.submission;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/submissions")
public class SubmissionController {
    private static final String DEMO_USER = "demo-user";
    private final SubmissionService service;

    public SubmissionController(SubmissionService service) {
        this.service = service;
    }

    @PostMapping
    public SubmissionView create(@Valid @RequestBody CreateSubmission request) {
        return service.create(request, DEMO_USER);
    }

    @GetMapping("/{id}")
    public SubmissionView get(@PathVariable String id) {
        return service.get(id);
    }

    @GetMapping
    public List<SubmissionView> recent() {
        return service.recent(DEMO_USER);
    }
}
