package com.example.backend.reseñas;

import com.example.backend.firestore.FirestoreCollections;
import com.example.backend.firestore.FirestoreFutures;
import com.google.cloud.firestore.Firestore;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Repository;

@Repository
public class ReseñaRepository {

  private final Firestore db;

  public ReseñaRepository(Firestore db) {
    this.db = db;
  }

  public List<ReseñaEntity> findAll() {
    return FirestoreFutures.get(db.collection(FirestoreCollections.RESEÑAS).get())
        .getDocuments().stream().map(this::toEntity).toList();
  }

  public List<ReseñaEntity> findByTourId(String tourId) {
    return FirestoreFutures.get(
            db.collection(FirestoreCollections.RESEÑAS)
                .whereEqualTo("tourId", tourId).get())
        .getDocuments().stream().map(this::toEntity).toList();
  }

  public ReseñaEntity getOrThrow(String id) {
    var doc = FirestoreFutures.get(
        db.collection(FirestoreCollections.RESEÑAS).document(id).get());
    if (!doc.exists()) throw new NoSuchElementException("Reseña no encontrada: " + id);
    return toEntity(doc);
  }

  public ReseñaEntity save(ReseñaEntity e) {
    FirestoreFutures.get(
        db.collection(FirestoreCollections.RESEÑAS).document(e.getId()).set(e));
    return e;
  }

  public void deleteById(String id) {
    FirestoreFutures.get(
        db.collection(FirestoreCollections.RESEÑAS).document(id).delete());
  }

  public boolean existsById(String id) {
    return FirestoreFutures.get(
        db.collection(FirestoreCollections.RESEÑAS).document(id).get()).exists();
  }

  private ReseñaEntity toEntity(com.google.cloud.firestore.DocumentSnapshot doc) {
    ReseñaEntity e = new ReseñaEntity();
    e.setId(doc.getId());
    e.setTourId(doc.getString("tourId"));
    e.setClienteId(doc.getString("clienteId"));
    e.setCalificacion(doc.getLong("calificacion") != null ? doc.getLong("calificacion").intValue() : 0);
    e.setTitulo(doc.getString("titulo"));
    e.setComentario(doc.getString("comentario"));
    e.setVerificada(Boolean.TRUE.equals(doc.getBoolean("verificada")));
    if (doc.getTimestamp("fecha") != null)
      e.setFecha(doc.getTimestamp("fecha").toDate().toInstant());
    return e;
  }
}
