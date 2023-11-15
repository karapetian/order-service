package com.imeasystems.orderservice.exception.handler;

import com.imeasystems.orderservice.exception.handler.dto.ApiError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(EntityNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND)
            .body(new ApiError(NOT_FOUND.value(), exception.getMessage(), Collections.emptyList()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
        ConstraintViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        List<String> messages = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
            String message = constraintViolation.getMessage();
            messages.add(message);
        }
        response.put("errors", messages);
        return ResponseEntity.status(BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<List<String>> handleDataIntegrityViolationException(
        DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolation Exception occurred", ex);
        String message = ex.getCause().getCause().getMessage();
        return ResponseEntity.status(BAD_REQUEST).body(Collections.singletonList(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception exception) {
        log.error("Exception occurred", exception);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
            .body(new ApiError(INTERNAL_SERVER_ERROR.value(), exception.getMessage(),
                Collections.emptyList()));
    }
}