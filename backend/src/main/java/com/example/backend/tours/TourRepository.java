package com.example.backend.tours;

import com.example.backend.firestore.FirestoreCollections;
import com.example.backend.firestore.FirestoreFutures;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Repository;

@Repository
public class TourRepository {

  private final Firestore db;

  public TourRepository(Firestore db) {
    this.db = db;
  }

  public List<TourEntity> findAll() {
    return FirestoreFutures.get(db.collection(FirestoreCollections.TOURS).get())
        .getDocuments()
        .stream()
        .map(this::toEntity)
        .toList();
  }

  public List<TourEntity> findByDestinoId(String destinoId) {
    return FirestoreFutures.get(
            db.collection(FirestoreCollections.TOURS)
                .whereEqualTo("destinoId", destinoId)
                .get())
        .getDocuments()
        .stream()
        .map(this::toEntity)
        .toList();
  }

  public TourEntity getOrThrow(String id) {
    var doc = FirestoreFutures.get(
        db.collection(FirestoreCollections.TOURS).document(id).get());
    if (!doc.exists()) throw new NoSuchElementException("Tour no encontrado: " + id);
    return toEntity(doc);
  }

  public TourEntity save(TourEntity e) {
    FirestoreFutures.get(
        db.collection(FirestoreCollections.TOURS).document(e.getId()).set(e));
    return e;
  }

  public void deleteById(String id) {
    FirestoreFutures.get(
        db.collection(FirestoreCollections.TOURS).document(id).delete());
  }

  public boolean existsById(String id) {
    return FirestoreFutures.get(
        db.collection(FirestoreCollections.TOURS).document(id).get()).exists();
  }

  public long count() {
    return FirestoreFutures.get(db.collection(FirestoreCollections.TOURS).get())
        .size();
  }

  private TourEntity toEntity(com.google.cloud.firestore.DocumentSnapshot doc) {
    TourEntity e = new TourEntity();
    e.setId(doc.getId());
    e.setNombre(doc.getString("nombre"));
    e.setDescripcion(doc.getString("descripcion"));
    e.setDestinoId(doc.getString("destinoId"));
    e.setPrecioAdulto(doc.getDouble("precioAdulto"));
    e.setPrecioNino(doc.getDouble("precioNino"));
    e.setDuracionDias(doc.getLong("duracionDias") != null ? doc.getLong("duracionDias").intValue() : 0);
    e.setCuposTotal(doc.getLong("cuposTotal") != null ? doc.getLong("cuposTotal").intValue() : 0);
    e.setCuposDisponibles(doc.getLong("cuposDisponibles") != null ? doc.getLong("cuposDisponibles").intValue() : 0);
    e.setDificultad(doc.getString("dificultad"));
    e.setIncluye(doc.getString("incluye"));
    e.setNoIncluye(doc.getString("noIncluye"));
    e.setImagenUrl(doc.getString("imagenUrl"));
    e.setActivo(Boolean.TRUE.equals(doc.getBoolean("activo")));
    if (doc.getTimestamp("creadoEn") != null)
      e.setCreadoEn(doc.getTimestamp("creadoEn").toDate().toInstant());
    return e;
  }
}
