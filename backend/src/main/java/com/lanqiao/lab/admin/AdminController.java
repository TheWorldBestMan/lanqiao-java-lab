package com.lanqiao.lab.admin;

import com.lanqiao.lab.problem.LicenseStatus;
import com.lanqiao.lab.problem.ProblemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final ProblemRepository problems;

    public AdminController(ProblemRepository problems) {
        this.problems = problems;
    }

    @GetMapping("/content-audit")
    public ContentAudit contentAudit() {
        var all = problems.findAll();
        Map<LicenseStatus, Long> byLicense = all.stream()
                .collect(Collectors.groupingBy(p -> p.getLicenseStatus(), Collectors.counting()));
        long publishable = all.stream().filter(p -> p.isContentAvailable()
                && (p.getLicenseStatus() == LicenseStatus.ORIGINAL || p.getLicenseStatus() == LicenseStatus.AUTHORIZED)).count();
        return new ContentAudit(all.size(), publishable, all.size() - publishable, byLicense);
    }

    public record ContentAudit(long total, long publishable, long blocked, Map<LicenseStatus, Long> byLicense) {}
}

