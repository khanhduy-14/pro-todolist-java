package com.khanhduy14.todolist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());

        return switch (ex) {
            case ResponseStatusException rse -> {
                body.put("status", rse.getStatusCode().value());
                body.put("error", rse.getStatusCode().toString());
                body.put("message", rse.getReason());
                yield new ResponseEntity<>(body, rse.getStatusCode());
            }
            case org.springframework.web.bind.MethodArgumentNotValidException manv -> {
                body.put("status", 400);
                body.put("error", "Bad Request");
                body.put("message", manv.getBindingResult().getAllErrors()
                        .stream()
                        .map(err -> err.getDefaultMessage())
                        .toList());
                yield ResponseEntity.badRequest().body(body);
            }
            case org.springframework.beans.TypeMismatchException tme -> {
                body.put("status", 400);
                body.put("error", "Bad Request");
                body.put("message", "Failed to convert parameter '" + tme.getPropertyName() + "' with value '" + tme.getValue() + "'");
                yield ResponseEntity.badRequest().body(body);
            }
            default -> {
                body.put("status", 500);
                body.put("error", "Internal Server Error");
                body.put("message", ex.getMessage());
                yield ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
            }
        };
    }

}