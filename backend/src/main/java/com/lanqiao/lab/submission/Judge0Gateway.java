package com.lanqiao.lab.submission;

import com.fasterxml.jackson.databind.JsonNode;
import com.lanqiao.lab.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class Judge0Gateway implements JudgeGateway {
    private final Judge0Properties properties;
    private final RestClient client;

    public Judge0Gateway(Judge0Properties properties, RestClient.Builder builder) {
        this.properties = properties;
        RestClient.Builder configured = builder.baseUrl(properties.baseUrl());
        if (properties.authToken() != null && !properties.authToken().isBlank()) {
            configured.defaultHeader("X-Auth-Token", properties.authToken());
        }
        this.client = configured.build();
    }

    @Override
    public JudgeResult judge(Problem problem, String sourceCode) {
        if (!properties.enabled()) {
            return new JudgeResult(SubmissionStatus.JUDGE_UNAVAILABLE, null, null, null,
                    "Judge0 尚未配置；出于安全考虑，系统不会在业务服务器本机执行代码。");
        }

        Map<String, Object> request = new LinkedHashMap<>();
        request.put("source_code", encode(sourceCode));
        request.put("language_id", properties.javaLanguageId());
        request.put("stdin", encode(nullToEmpty(problem.getSampleInput())));
        request.put("expected_output", encode(nullToEmpty(problem.getSampleOutput())));
        request.put("cpu_time_limit", Math.max(0.1, problem.getTimeLimitMs() / 1000.0));
        request.put("wall_time_limit", Math.max(1.0, problem.getTimeLimitMs() / 500.0));
        request.put("memory_limit", problem.getMemoryLimitMb() * 1024);
        request.put("stack_limit", Math.min(problem.getMemoryLimitMb() * 1024, 64 * 1024));
        request.put("max_file_size", 1024);
        request.put("max_processes_and_or_threads", 32);
        request.put("enable_network", false);

        try {
            JsonNode created = client.post()
                    .uri("/submissions?base64_encoded=true&wait=false")
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(request)
                    .retrieve()
                    .body(JsonNode.class);
            String token = created == null ? null : created.path("token").asText(null);
            if (token == null || token.isBlank()) {
                return unavailable("Judge0 未返回提交令牌");
            }

            for (int attempt = 0; attempt < properties.pollAttempts(); attempt++) {
                JsonNode result = client.get()
                        .uri("/submissions/{token}?base64_encoded=true&fields=status,time,memory,stderr,compile_output,message", token)
                        .retrieve()
                        .body(JsonNode.class);
                int statusId = result == null ? 0 : result.path("status").path("id").asInt(0);
                if (statusId > 2) return mapResult(token, result, statusId);
                Thread.sleep(properties.pollDelayMs());
            }
            return new JudgeResult(SubmissionStatus.RUNNING, null, null, token, "判题仍在进行，请稍后刷新。");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return unavailable("判题等待被中断");
        } catch (Exception ex) {
            return unavailable("Judge0 暂时不可用，请稍后重试。");
        }
    }

    private JudgeResult mapResult(String token, JsonNode result, int statusId) {
        SubmissionStatus status = switch (statusId) {
            case 3 -> SubmissionStatus.ACCEPTED;
            case 4 -> SubmissionStatus.WRONG_ANSWER;
            case 5 -> SubmissionStatus.TIME_LIMIT_EXCEEDED;
            case 6 -> SubmissionStatus.COMPILE_ERROR;
            case 7, 8, 9, 10, 11, 12 -> SubmissionStatus.RUNTIME_ERROR;
            default -> SubmissionStatus.INTERNAL_ERROR;
        };
        double seconds = result.path("time").asDouble(0);
        Integer runtime = seconds <= 0 ? null : (int) Math.round(seconds * 1000);
        Integer memory = result.path("memory").isNumber() ? result.path("memory").asInt() : null;
        String detail = firstDecoded(result, "compile_output", "stderr", "message");
        if (detail == null || detail.isBlank()) detail = result.path("status").path("description").asText("判题完成");
        return new JudgeResult(status, runtime, memory, token, trim(detail, 500));
    }

    private String firstDecoded(JsonNode node, String... fields) {
        for (String field : fields) {
            String value = node.path(field).asText(null);
            if (value != null && !value.isBlank()) {
                try { return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8); }
                catch (IllegalArgumentException ignored) { return value; }
            }
        }
        return null;
    }

    private JudgeResult unavailable(String message) {
        return new JudgeResult(SubmissionStatus.JUDGE_UNAVAILABLE, null, null, null, message);
    }

    private String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String nullToEmpty(String value) { return value == null ? "" : value; }
    private String trim(String value, int max) { return value.length() <= max ? value : value.substring(0, max); }
}

