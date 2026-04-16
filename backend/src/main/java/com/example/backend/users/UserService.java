package com.example.backend.users;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository repo;

  public UserService(UserRepository repo) {
    this.repo = repo;
  }

  public List<UserResponse> list() {
    return repo.findAllOrderByNombre().stream().map(UserResponse::from).toList();
  }

  public UserResponse get(String id) {
    return UserResponse.from(repo.getOrThrow(id));
  }

  public UserResponse create(UserCreateRequest req) {
    if (repo.existsByEmailIgnoreCase(req.email())) {
      throw new IllegalArgumentException("Ya existe un usuario con ese email");
    }
    UserEntity e = new UserEntity();
    e.setId(UUID.randomUUID().toString());
    e.setNombre(req.nombre());
    e.setEmail(req.email());
    e.setTelefono(req.telefono());
    e.setRol(req.rol());
    e.setReservas(req.reservas());
    return UserResponse.from(repo.save(e));
  }

  public UserResponse update(String id, UserUpdateRequest req) {
    UserEntity e = repo.getOrThrow(id);

    if (!e.getEmail().equalsIgnoreCase(req.email()) && repo.existsByEmailIgnoreCase(req.email())) {
      throw new IllegalArgumentException("Ya existe un usuario con ese email");
    }

    e.setNombre(req.nombre());
    e.setEmail(req.email());
    e.setTelefono(req.telefono());
    e.setRol(req.rol());
    e.setReservas(req.reservas());
    return UserResponse.from(repo.save(e));
  }

  public void delete(String id) {
    if (!repo.existsById(id)) throw new NoSuchElementException("Usuario no encontrado");
    repo.deleteById(id);
  }
}

