package com.example.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseAdminConfig {

  @Value("${firebase.service-account-path}")
  private String serviceAccountPath;

  @Bean
  public Firestore firestore(@Value("${firebase.project-id}") String projectId) throws IOException {
    initFirebase(projectId);
    return FirestoreClient.getFirestore();
  }

  private void initFirebase(String projectId) throws IOException {
    if (!FirebaseApp.getApps().isEmpty()) return;

    GoogleCredentials credentials = loadCredentials();
    FirebaseOptions options =
      FirebaseOptions.builder()
        .setCredentials(credentials)
        .setProjectId(projectId)
        .build();
    FirebaseApp.initializeApp(options);
  }

  private GoogleCredentials loadCredentials() throws IOException {
    // 1. Variable de entorno FIREBASE_SERVICE_ACCOUNT_JSON (ruta absoluta)
    String envPath = System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON");
    if (envPath != null && !envPath.isBlank()) {
      try (InputStream is = new java.io.FileInputStream(envPath)) {
        return GoogleCredentials.fromStream(is);
      }
    }

    // 2. Desde resources/ (classpath)
    try (InputStream is = getClass().getClassLoader().getResourceAsStream(serviceAccountPath)) {
      if (is != null) {
        return GoogleCredentials.fromStream(is);
      }
    }

    // 3. ADC — Google Application Default Credentials
    return GoogleCredentials.getApplicationDefault();
  }
}
