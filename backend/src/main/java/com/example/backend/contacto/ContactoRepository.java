package com.example.backend.contacto;

import static com.example.backend.firestore.FirestoreFutures.get;

import com.example.backend.firestore.FirestoreCollections;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ContactoRepository {

  private final CollectionReference col;

  public ContactoRepository(Firestore db) {
    this.col = db.collection(FirestoreCollections.CONTACTO);
  }

  public List<ContactoMessageEntity> findAllOrderByCreatedAtDesc() {
    QuerySnapshot snap = get(col.orderBy("createdAt", com.google.cloud.firestore.Query.Direction.DESCENDING).get());
    return snap.getDocuments().stream().map(this::toEntity).toList();
  }

  public Optional<ContactoMessageEntity> findById(String id) {
    DocumentSnapshot doc = get(col.document(id).get());
    if (!doc.exists()) return Optional.empty();
    return Optional.of(toEntity(doc));
  }

  public boolean existsById(String id) {
    DocumentSnapshot doc = get(col.document(id).get());
    return doc.exists();
  }

  public ContactoMessageEntity save(ContactoMessageEntity e) {
    if (e.getId() == null || e.getId().isBlank()) {
      throw new IllegalArgumentException("id requerido");
    }
    get(col.document(e.getId()).set(toDoc(e)));
    return e;
  }

  public void deleteById(String id) {
    get(col.document(id).delete());
  }

  private ContactoMessageEntity toEntity(DocumentSnapshot d) {
    ContactoMessageEntity e = new ContactoMessageEntity();
    e.setId(d.getId());
    e.setNombre(string(d, "nombre"));
    e.setEmail(string(d, "email"));
    e.setMensaje(string(d, "mensaje"));
    Object created = d.get("createdAt");
    if (created instanceof com.google.cloud.Timestamp ts) {
      e.setCreatedAt(ts.toDate().toInstant());
    } else if (created instanceof String s && !s.isBlank()) {
      e.setCreatedAt(Instant.parse(s));
    } else {
      e.setCreatedAt(Instant.EPOCH);
    }
    return e;
  }

  private java.util.Map<String, Object> toDoc(ContactoMessageEntity e) {
    return java.util.Map.of(
        "nombre",
        e.getNombre(),
        "email",
        e.getEmail(),
        "mensaje",
        e.getMensaje(),
        "createdAt",
        com.google.cloud.Timestamp.ofTimeSecondsAndNanos(
            e.getCreatedAt().getEpochSecond(), e.getCreatedAt().getNano()));
  }

  private String string(DocumentSnapshot d, String field) {
    Object v = d.get(field);
    return v == null ? "" : String.valueOf(v);
  }
}

