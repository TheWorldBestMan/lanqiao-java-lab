CREATE TABLE problems (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    slug VARCHAR(80) NOT NULL UNIQUE,
    title VARCHAR(160) NOT NULL,
    summary VARCHAR(320) NOT NULL,
    description_md TEXT NOT NULL,
    difficulty VARCHAR(16) NOT NULL,
    source_type VARCHAR(32) NOT NULL,
    source_label VARCHAR(120) NOT NULL,
    source_ref VARCHAR(240),
    license_status VARCHAR(24) NOT NULL,
    content_available BOOLEAN NOT NULL,
    time_limit_ms INT NOT NULL,
    memory_limit_mb INT NOT NULL,
    template_code TEXT,
    sample_input TEXT,
    sample_output TEXT,
    tags VARCHAR(240) NOT NULL
);

CREATE TABLE submissions (
    id VARCHAR(36) PRIMARY KEY,
    problem_id BIGINT NOT NULL,
    user_key VARCHAR(80) NOT NULL,
    source_code TEXT NOT NULL,
    language VARCHAR(24) NOT NULL,
    status VARCHAR(32) NOT NULL,
    score INT,
    runtime_ms INT,
    memory_kb INT,
    judge_token VARCHAR(120),
    message VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_submission_problem FOREIGN KEY (problem_id) REFERENCES problems(id)
);

CREATE INDEX idx_submissions_user_created ON submissions(user_key, created_at);
CREATE INDEX idx_submissions_problem_status ON submissions(problem_id, status);

