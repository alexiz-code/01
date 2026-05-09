package com.example.backend.pagos;

import com.example.backend.firestore.FirestoreCollections;
import com.example.backend.firestore.FirestoreFutures;
import com.google.cloud.firestore.Firestore;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Repository;

@Repository
public class PagoRepository {

  private final Firestore db;

  public PagoRepository(Firestore db) {
    this.db = db;
  }

  public List<PagoEntity> findAll() {
    return FirestoreFutures.get(db.collection(FirestoreCollections.PAGOS).get())
        .getDocuments().stream().map(this::toEntity).toList();
  }

  public List<PagoEntity> findByReservaId(String reservaId) {
    return FirestoreFutures.get(
            db.collection(FirestoreCollections.PAGOS)
                .whereEqualTo("reservaId", reservaId).get())
        .getDocuments().stream().map(this::toEntity).toList();
  }

  public PagoEntity getOrThrow(String id) {
    var doc = FirestoreFutures.get(
        db.collection(FirestoreCollections.PAGOS).document(id).get());
    if (!doc.exists()) throw new NoSuchElementException("Pago no encontrado: " + id);
    return toEntity(doc);
  }

  public PagoEntity save(PagoEntity e) {
    FirestoreFutures.get(
        db.collection(FirestoreCollections.PAGOS).document(e.getId()).set(e));
    return e;
  }

  public void deleteById(String id) {
    FirestoreFutures.get(
        db.collection(FirestoreCollections.PAGOS).document(id).delete());
  }

  public boolean existsById(String id) {
    return FirestoreFutures.get(
        db.collection(FirestoreCollections.PAGOS).document(id).get()).exists();
  }

  private PagoEntity toEntity(com.google.cloud.firestore.DocumentSnapshot doc) {
    PagoEntity e = new PagoEntity();
    e.setId(doc.getId());
    e.setReservaId(doc.getString("reservaId"));
    e.setMonto(doc.getDouble("monto"));
    e.setMetodo(doc.getString("metodo"));
    e.setEstado(doc.getString("estado"));
    e.setReferencia(doc.getString("referencia"));
    e.setComprobanteUrl(doc.getString("comprobanteUrl"));
    if (doc.getTimestamp("fechaPago") != null)
      e.setFechaPago(doc.getTimestamp("fechaPago").toDate().toInstant());
    return e;
  }
}
