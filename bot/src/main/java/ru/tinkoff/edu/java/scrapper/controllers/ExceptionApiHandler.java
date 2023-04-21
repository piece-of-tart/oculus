package ru.tinkoff.edu.java.scrapper.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.tinkoff.edu.java.scrapper.dto.response.ApiErrorResponse;

import java.util.Arrays;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiErrorResponse> handleIncorrectBodyException(Exception e) {
        return ResponseEntity.badRequest().body(getApiErrorResponse("Incorrect query parameters", "400", e));
    }

    public static ApiErrorResponse getApiErrorResponse(String description, String code, Exception e) {
        return new ApiErrorResponse(
                description,
                code,
                e.getClass().getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList());
    }
}
