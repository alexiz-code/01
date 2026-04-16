package com.example.backend.users;

import static com.example.backend.firestore.FirestoreFutures.get;

import com.example.backend.firestore.FirestoreCollections;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  private final CollectionReference col;

  public UserRepository(Firestore db) {
    this.col = db.collection(FirestoreCollections.USERS);
  }

  public List<UserEntity> findAllOrderByNombre() {
    QuerySnapshot snap = get(col.orderBy("nombre").get());
    return snap.getDocuments().stream().map(this::toEntity).toList();
  }

  public Optional<UserEntity> findById(String id) {
    DocumentSnapshot doc = get(col.document(id).get());
    if (!doc.exists()) return Optional.empty();
    return Optional.of(toEntity(doc));
  }

  public boolean existsById(String id) {
    DocumentSnapshot doc = get(col.document(id).get());
    return doc.exists();
  }

  public Optional<UserEntity> findByEmailIgnoreCase(String email) {
    QuerySnapshot snap = get(col.whereEqualTo("emailLower", email.toLowerCase()).limit(1).get());
    if (snap.isEmpty()) return Optional.empty();
    return Optional.of(toEntity(snap.getDocuments().get(0)));
  }

  public boolean existsByEmailIgnoreCase(String email) {
    return findByEmailIgnoreCase(email).isPresent();
  }

  public UserEntity save(UserEntity e) {
    if (e.getId() == null || e.getId().isBlank()) {
      throw new IllegalArgumentException("id requerido");
    }
    get(col.document(e.getId()).set(toDoc(e)));
    return e;
  }

  public void deleteById(String id) {
    get(col.document(id).delete());
  }

  public long count() {
    QuerySnapshot snap = get(col.limit(1).get());
    // If there's at least one doc, count is > 0; Firestore count aggregation requires extra API
    return snap.isEmpty() ? 0 : 1;
  }

  public UserEntity getOrThrow(String id) {
    return findById(id).orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
  }

  private UserEntity toEntity(DocumentSnapshot d) {
    UserEntity e = new UserEntity();
    e.setId(d.getId());
    e.setNombre(string(d, "nombre"));
    e.setEmail(string(d, "email"));
    e.setTelefono(string(d, "telefono"));
    String rol = string(d, "rol");
    e.setRol(rol == null || rol.isBlank() ? UserRole.cliente : UserRole.valueOf(rol));
    Long reservas = d.getLong("reservas");
    e.setReservas(reservas == null ? 0 : reservas.intValue());
    return e;
  }

  private java.util.Map<String, Object> toDoc(UserEntity e) {
    return java.util.Map.of(
        "nombre",
        e.getNombre(),
        "email",
        e.getEmail(),
        "emailLower",
        e.getEmail().toLowerCase(),
        "telefono",
        e.getTelefono(),
        "rol",
        e.getRol().name(),
        "reservas",
        e.getReservas());
  }

  private String string(DocumentSnapshot d, String field) {
    Object v = d.get(field);
    return v == null ? "" : String.valueOf(v);
  }
}

