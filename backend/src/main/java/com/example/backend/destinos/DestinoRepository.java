package com.example.backend.destinos;

import static com.example.backend.firestore.FirestoreFutures.get;

import com.example.backend.firestore.FirestoreCollections;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DestinoRepository {

  private final CollectionReference col;

  public DestinoRepository(Firestore db) {
    this.col = db.collection(FirestoreCollections.DESTINOS);
  }

  public List<DestinoEntity> findAllOrderByTitle() {
    QuerySnapshot snap = get(col.orderBy("title").get());
    return snap.getDocuments().stream().map(this::toEntity).toList();
  }

  public DestinoEntity save(DestinoEntity e) {
    if (e.getId() == null || e.getId().isBlank()) {
      throw new IllegalArgumentException("id requerido");
    }
    get(col.document(e.getId()).set(toDoc(e)));
    return e;
  }

  public long count() {
    QuerySnapshot snap = get(col.limit(1).get());
    return snap.isEmpty() ? 0 : 1;
  }

  private DestinoEntity toEntity(DocumentSnapshot d) {
    DestinoEntity e = new DestinoEntity();
    e.setId(d.getId());
    e.setLabel(string(d, "label"));
    e.setTitle(string(d, "title"));
    e.setDesc(string(d, "desc"));
    e.setName(string(d, "name"));
    e.setBg(string(d, "bg"));
    e.setThumb(string(d, "thumb"));
    return e;
  }

  private java.util.Map<String, Object> toDoc(DestinoEntity e) {
    return java.util.Map.of(
        "label",
        e.getLabel(),
        "title",
        e.getTitle(),
        "desc",
        e.getDesc(),
        "name",
        e.getName(),
        "bg",
        e.getBg(),
        "thumb",
        e.getThumb());
  }

  private String string(DocumentSnapshot d, String field) {
    Object v = d.get(field);
    return v == null ? "" : String.valueOf(v);
  }
}

