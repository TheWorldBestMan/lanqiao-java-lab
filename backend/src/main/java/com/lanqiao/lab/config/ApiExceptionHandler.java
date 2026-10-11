package com.lanqiao.lab.config;

import com.lanqiao.lab.problem.ProblemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ProblemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFound(RuntimeException ex) { return error("NOT_FOUND", ex.getMessage()); }

    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError badRequest(Exception ex) { return error("BAD_REQUEST", ex.getMessage()); }

    private ApiError error(String code, String message) {
        return new ApiError(code, message, Instant.now());
    }

    public record ApiError(String code, String message, Instant timestamp) {}
}

