package com.example.backend.contacto;

import java.time.Instant;

public record ContactoResponse(
    String id, String nombre, String email, String mensaje, Instant createdAt) {
  public static ContactoResponse from(ContactoMessageEntity e) {
    return new ContactoResponse(
        e.getId(), e.getNombre(), e.getEmail(), e.getMensaje(), e.getCreatedAt());
  }
}

