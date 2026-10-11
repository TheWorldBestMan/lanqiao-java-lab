package com.lanqiao.lab.submission;

import com.lanqiao.lab.problem.Problem;

public interface JudgeGateway {
    JudgeResult judge(Problem problem, String sourceCode);
}

