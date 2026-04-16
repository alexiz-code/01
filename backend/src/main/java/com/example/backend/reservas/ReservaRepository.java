package com.example.backend.reservas;

import static com.example.backend.firestore.FirestoreFutures.get;

import com.example.backend.firestore.FirestoreCollections;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ReservaRepository {

  private final CollectionReference col;

  public ReservaRepository(Firestore db) {
    this.col = db.collection(FirestoreCollections.RESERVAS);
  }

  public List<ReservaEntity> findAllOrderByCreatedAtDesc() {
    QuerySnapshot snap = get(col.orderBy("createdAt", com.google.cloud.firestore.Query.Direction.DESCENDING).get());
    return snap.getDocuments().stream().map(this::toEntity).toList();
  }

  public Optional<ReservaEntity> findById(String id) {
    DocumentSnapshot doc = get(col.document(id).get());
    if (!doc.exists()) return Optional.empty();
    return Optional.of(toEntity(doc));
  }

  public boolean existsById(String id) {
    DocumentSnapshot doc = get(col.document(id).get());
    return doc.exists();
  }

  public ReservaEntity save(ReservaEntity e) {
    if (e.getId() == null || e.getId().isBlank()) {
      throw new IllegalArgumentException("id requerido");
    }
    get(col.document(e.getId()).set(toDoc(e)));
    return e;
  }

  public void deleteById(String id) {
    get(col.document(id).delete());
  }

  private ReservaEntity toEntity(DocumentSnapshot d) {
    ReservaEntity e = new ReservaEntity();
    e.setId(d.getId());
    e.setNombre(string(d, "nombre"));
    e.setApellido(string(d, "apellido"));
    e.setEmail(string(d, "email"));
    e.setTelefono(string(d, "telefono"));
    e.setDestino(string(d, "destino"));
    e.setClase(string(d, "clase"));
    e.setNotas(optString(d, "notas"));

    String fechaIda = optString(d, "fechaIda");
    if (!fechaIda.isBlank()) e.setFechaIda(LocalDate.parse(fechaIda));
    String fechaVuelta = optString(d, "fechaVuelta");
    if (!fechaVuelta.isBlank()) e.setFechaVuelta(LocalDate.parse(fechaVuelta));

    Long pasajeros = d.getLong("pasajeros");
    e.setPasajeros(pasajeros == null ? 1 : pasajeros.intValue());

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

  private java.util.Map<String, Object> toDoc(ReservaEntity e) {
    java.util.Map<String, Object> m = new java.util.HashMap<>();
    m.put("nombre", e.getNombre());
    m.put("apellido", e.getApellido());
    m.put("email", e.getEmail());
    m.put("telefono", e.getTelefono());
    m.put("destino", e.getDestino());
    m.put("fechaIda", e.getFechaIda().toString());
    m.put("fechaVuelta", e.getFechaVuelta().toString());
    m.put("pasajeros", e.getPasajeros());
    m.put("clase", e.getClase());
    m.put("notas", e.getNotas() == null ? "" : e.getNotas());
    m.put(
        "createdAt",
        com.google.cloud.Timestamp.ofTimeSecondsAndNanos(
            e.getCreatedAt().getEpochSecond(), e.getCreatedAt().getNano()));
    return m;
  }

  private String string(DocumentSnapshot d, String field) {
    Object v = d.get(field);
    return v == null ? "" : String.valueOf(v);
  }

  private String optString(DocumentSnapshot d, String field) {
    Object v = d.get(field);
    return v == null ? "" : String.valueOf(v);
  }
}

