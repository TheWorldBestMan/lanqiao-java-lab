package com.lanqiao.lab.submission;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, String> {
    List<Submission> findTop20ByUserKeyOrderByCreatedAtDesc(String userKey);
}

