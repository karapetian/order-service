package com.imeasystems.orderservice.exception.handler;

import com.imeasystems.orderservice.exception.handler.dto.ApiError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {

    private ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void handleNotFoundExceptionTest() {
        final String expected = "ApiError(code=404, message=Entity with id 1 not found, errors=[])";
        final String message = "Entity with id 1 not found";
        final ResponseEntity<ApiError> responseEntity = handler.handleNotFoundException(
                new EntityNotFoundException(message));
        final String actual = String.valueOf(responseEntity.getBody());

        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(expected, actual);
    }

    @Test
    void handleExceptionTest() {
        final String expected = "ApiError(code=500, message=Error, errors=[])";
        final String message = "Error";
        final ResponseEntity<?> responseEntity = handler.handleException(new Exception(message));
        final String actual = String.valueOf(responseEntity.getBody());

        assertEquals(INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(expected, actual);
    }

    @Test
    void handleMethodArgumentNotValidExceptionTest() {
        final String expected = "{field1=is empty}";

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        final MethodArgumentNotValidException methodArgumentNotValidException = new MethodArgumentNotValidException(
                mock(MethodParameter.class), bindingResult);
        MethodArgumentNotValidException mock = Mockito.spy(methodArgumentNotValidException);

        when(bindingResult.getAllErrors()).thenReturn(
                List.of(new FieldError("", "field1", "is empty")));

        final ResponseEntity<Map<String, String>> responseEntity = handler.handleMethodArgumentNotValidException(
                mock);
        final String actual = String.valueOf(responseEntity.getBody());

        assertEquals(UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertEquals(expected, actual);
    }

    @Test
    void handleValidationExceptionsTest() {
        final ConstraintViolationException mock = mock(ConstraintViolationException.class);
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolation mockedViolation = mock(ConstraintViolation.class);
        violations.add(mockedViolation);

        when(mock.getConstraintViolations()).thenReturn(violations);

        final ResponseEntity<Map<String, Object>> responseEntity = handler.handleValidationExceptions(
                mock);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
    }
}