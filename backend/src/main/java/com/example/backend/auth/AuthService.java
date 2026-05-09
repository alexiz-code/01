package com.example.backend.auth;

import com.example.backend.users.UserEntity;
import com.example.backend.users.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserRepository userRepo;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public AuthService(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  public LoginResponse login(LoginRequest req) {
    UserEntity user = userRepo
        .findByEmailIgnoreCase(req.email())
        .orElseThrow(() -> new IllegalArgumentException("Email o contraseña incorrectos"));

    if (user.getPasswordHash() == null || user.getPasswordHash().isBlank())
      throw new IllegalArgumentException("Usuario sin contraseña configurada");

    if (!encoder.matches(req.password(), user.getPasswordHash()))
      throw new IllegalArgumentException("Email o contraseña incorrectos");

    return new LoginResponse(
        user.getId(),
        user.getNombre(),
        user.getEmail(),
        user.getTelefono(),
        user.getRol());
  }
}
