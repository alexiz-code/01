package com.example.backend.users;

public record UserResponse(
  String id,
  String nombre,
  String email,
  String telefono,
  UserRole rol) {

  public static UserResponse from(UserEntity e) {
    return new UserResponse(
      e.getId(),
      e.getNombre(),
      e.getEmail(),
      e.getTelefono(),
      e.getRol());
  }
}
