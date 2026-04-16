package com.example.backend.api;

import java.time.Instant;
import java.util.List;

public record ApiError(String message, Instant timestamp, List<FieldErrorItem> fieldErrors) {
  public record FieldErrorItem(String field, String message) {}
}

