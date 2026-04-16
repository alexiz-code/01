package com.example.backend.api;

import com.example.backend.api.ApiError.FieldErrorItem;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ApiError> handleNotFound(NoSuchElementException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ApiError(ex.getMessage(), Instant.now(), List.of()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
    List<FieldErrorItem> fields =
        ex.getBindingResult().getFieldErrors().stream()
            .map(this::toFieldError)
            .toList();
    return ResponseEntity.badRequest()
        .body(new ApiError("Validación inválida", Instant.now(), fields));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex) {
    return ResponseEntity.badRequest()
        .body(new ApiError(ex.getMessage(), Instant.now(), List.of()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ApiError("Error interno", Instant.now(), List.of()));
  }

  private FieldErrorItem toFieldError(FieldError fe) {
    return new FieldErrorItem(fe.getField(), fe.getDefaultMessage());
  }
}

