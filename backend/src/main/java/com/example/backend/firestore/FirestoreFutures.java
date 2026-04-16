package com.example.backend.firestore;

import com.google.api.core.ApiFuture;
import java.util.concurrent.ExecutionException;

public final class FirestoreFutures {
  private FirestoreFutures() {}

  public static <T> T get(ApiFuture<T> f) {
    try {
      return f.get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e.getCause() != null ? e.getCause() : e);
    }
  }
}

