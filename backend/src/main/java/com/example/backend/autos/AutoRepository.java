package com.example.backend.autos;

import com.example.backend.firestore.FirestoreCollections;
import com.example.backend.firestore.FirestoreFutures;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Repository;

@Repository
public class AutoRepository {

  private final Firestore db;

  public AutoRepository(Firestore db) {
    this.db = db;
  }

  public List<AutoEntity> findAll() {
    return FirestoreFutures.get(db.collection(FirestoreCollections.AUTOS).get())
      .getDocuments().stream().map(this::toEntity).toList();
  }

  public List<AutoEntity> findByEstado(String estado) {
    return FirestoreFutures.get(
        db.collection(FirestoreCollections.AUTOS)
          .whereEqualTo("estado", estado).get())
      .getDocuments().stream().map(this::toEntity).toList();
  }

  public AutoEntity getOrThrow(String id) {
    var doc = FirestoreFutures.get(
      db.collection(FirestoreCollections.AUTOS).document(id).get());
    if (!doc.exists()) throw new NoSuchElementException("Auto no encontrado: " + id);
    return toEntity(doc);
  }

  public AutoEntity save(AutoEntity e) {
    FirestoreFutures.get(
      db.collection(FirestoreCollections.AUTOS).document(e.getId()).set(e));
    return e;
  }

  public void deleteById(String id) {
    FirestoreFutures.get(
      db.collection(FirestoreCollections.AUTOS).document(id).delete());
  }

  public boolean existsById(String id) {
    return FirestoreFutures.get(
      db.collection(FirestoreCollections.AUTOS).document(id).get()).exists();
  }

  public boolean existsByPlaca(String placa) {
    return !FirestoreFutures.get(
        db.collection(FirestoreCollections.AUTOS)
          .whereEqualTo("placa", placa).get())
      .isEmpty();
  }

  public long count() {
    return FirestoreFutures.get(db.collection(FirestoreCollections.AUTOS).get()).size();
  }

  private AutoEntity toEntity(DocumentSnapshot doc) {
    AutoEntity e = new AutoEntity();
    e.setId(doc.getId());
    e.setPlaca(doc.getString("placa"));
    e.setMarca(doc.getString("marca"));
    e.setModelo(doc.getString("modelo"));
    e.setColor(doc.getString("color"));
    e.setAnioFabrica(doc.getLong("anioFabrica") != null ? doc.getLong("anioFabrica").intValue() : 0);
    e.setCantidadAsiento(doc.getLong("cantidadAsiento") != null ? doc.getLong("cantidadAsiento").intValue() : 0);
    e.setTipo(doc.getString("tipo"));
    e.setConductor(doc.getString("conductor"));
    e.setEstado(doc.getString("estado"));
    return e;
  }
}
