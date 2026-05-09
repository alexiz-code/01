package com.example.backend.reservas;

import static com.example.backend.firestore.FirestoreFutures.get;

import com.example.backend.firestore.FirestoreCollections;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ReservaRepository {

  private final CollectionReference col;

  public ReservaRepository(Firestore db) {
    this.col = db.collection(FirestoreCollections.RESERVAS);
  }

  public List<ReservaEntity> findAllOrderByCreatedAtDesc() {
    QuerySnapshot snap = get(col.orderBy("createdAt",
      com.google.cloud.firestore.Query.Direction.DESCENDING).get());
    return snap.getDocuments().stream().map(this::toEntity).toList();
  }

  public Optional<ReservaEntity> findById(String id) {
    DocumentSnapshot doc = get(col.document(id).get());
    if (!doc.exists()) return Optional.empty();
    return Optional.of(toEntity(doc));
  }

  public boolean existsById(String id) {
    return get(col.document(id).get()).exists();
  }

  public ReservaEntity save(ReservaEntity e) {
    if (e.getId() == null || e.getId().isBlank())
      throw new IllegalArgumentException("id requerido");
    get(col.document(e.getId()).set(toDoc(e)));
    return e;
  }

  public void deleteById(String id) {
    get(col.document(id).delete());
  }

  private ReservaEntity toEntity(DocumentSnapshot d) {
    ReservaEntity e = new ReservaEntity();
    e.setId(d.getId());
    e.setNombre(safeStr(d, "nombre"));
    e.setApellido(safeStr(d, "apellido"));
    e.setEmail(safeStr(d, "email"));
    e.setTelefono(safeStr(d, "telefono"));
    e.setDestino(safeStr(d, "destino"));
    e.setNotas(safeStr(d, "notas"));   // puede ser vacío/null — sin @NotBlank

    // Fecha de ida
    String fechaIda = safeStr(d, "fechaIda");
    if (!fechaIda.isBlank()) {
      try { e.setFechaIda(LocalDate.parse(fechaIda)); } catch (Exception ignored) {}
    }

    // Fecha de vuelta
    String fechaVuelta = safeStr(d, "fechaVuelta");
    if (!fechaVuelta.isBlank()) {
      try { e.setFechaVuelta(LocalDate.parse(fechaVuelta)); } catch (Exception ignored) {}
    }

    // Pasajeros — puede venir como Long o String
    Object pas = d.get("pasajeros");
    if (pas instanceof Long l)         e.setPasajeros(l.intValue());
    else if (pas instanceof String s)  { try { e.setPasajeros(Integer.parseInt(s)); } catch (Exception ignored) { e.setPasajeros(1); } }
    else                               e.setPasajeros(1);

    // createdAt — puede ser Timestamp, String o null
    Object created = d.get("createdAt");
    if (created instanceof Timestamp ts)       e.setCreatedAt(ts.toDate().toInstant());
    else if (created instanceof String s && !s.isBlank()) {
      try { e.setCreatedAt(Instant.parse(s)); } catch (Exception ignored) { e.setCreatedAt(Instant.EPOCH); }
    } else {
      e.setCreatedAt(Instant.EPOCH);
    }

    return e;
  }

  private Map<String, Object> toDoc(ReservaEntity e) {
    Map<String, Object> m = new HashMap<>();
    m.put("nombre",      e.getNombre());
    m.put("apellido",    e.getApellido());
    m.put("email",       e.getEmail());
    m.put("telefono",    e.getTelefono());
    m.put("destino",     e.getDestino());
    m.put("fechaIda",    e.getFechaIda() != null ? e.getFechaIda().toString() : "");
    m.put("fechaVuelta", e.getFechaVuelta() != null ? e.getFechaVuelta().toString() : "");
    m.put("pasajeros",   e.getPasajeros());
    m.put("notas",       e.getNotas() != null ? e.getNotas() : "");
    m.put("createdAt",   Timestamp.ofTimeSecondsAndNanos(
      e.getCreatedAt().getEpochSecond(), e.getCreatedAt().getNano()));
    return m;
  }

  private String safeStr(DocumentSnapshot d, String field) {
    Object v = d.get(field);
    return v == null ? "" : String.valueOf(v);
  }
}
