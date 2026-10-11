package com.lanqiao.lab.problem;

import jakarta.persistence.*;

@Entity
@Table(name = "problems")
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String slug;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(nullable = false, length = 320)
    private String summary;

    @Lob
    @Column(name = "description_md", nullable = false)
    private String descriptionMd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Difficulty difficulty;

    @Column(name = "source_type", nullable = false, length = 32)
    private String sourceType;

    @Column(name = "source_label", nullable = false, length = 120)
    private String sourceLabel;

    @Column(name = "source_ref", length = 240)
    private String sourceRef;

    @Enumerated(EnumType.STRING)
    @Column(name = "license_status", nullable = false, length = 24)
    private LicenseStatus licenseStatus;

    @Column(name = "content_available", nullable = false)
    private boolean contentAvailable;

    @Column(name = "time_limit_ms", nullable = false)
    private int timeLimitMs;

    @Column(name = "memory_limit_mb", nullable = false)
    private int memoryLimitMb;

    @Lob
    @Column(name = "template_code")
    private String templateCode;

    @Lob
    @Column(name = "sample_input")
    private String sampleInput;

    @Lob
    @Column(name = "sample_output")
    private String sampleOutput;

    @Column(nullable = false, length = 240)
    private String tags;

    public Long getId() { return id; }
    public String getSlug() { return slug; }
    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public String getDescriptionMd() { return descriptionMd; }
    public Difficulty getDifficulty() { return difficulty; }
    public String getSourceType() { return sourceType; }
    public String getSourceLabel() { return sourceLabel; }
    public String getSourceRef() { return sourceRef; }
    public LicenseStatus getLicenseStatus() { return licenseStatus; }
    public boolean isContentAvailable() { return contentAvailable; }
    public int getTimeLimitMs() { return timeLimitMs; }
    public int getMemoryLimitMb() { return memoryLimitMb; }
    public String getTemplateCode() { return templateCode; }
    public String getSampleInput() { return sampleInput; }
    public String getSampleOutput() { return sampleOutput; }
    public String getTags() { return tags; }
}

