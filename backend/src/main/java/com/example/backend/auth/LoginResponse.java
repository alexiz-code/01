package com.example.backend.auth;

import com.example.backend.users.UserRole;

public record LoginResponse(
    String id,
    String nombre,
    String email,
    String telefono,
    UserRole rol) {}
