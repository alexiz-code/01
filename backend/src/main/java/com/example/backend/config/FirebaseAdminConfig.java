package com.example.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseAdminConfig {

  @Bean
  public Firestore firestore(@Value("${firebase.project-id}") String projectId) throws IOException {
    initFirebase(projectId);
    return FirestoreClient.getFirestore();
  }

  private void initFirebase(String projectId) throws IOException {
    if (!FirebaseApp.getApps().isEmpty()) return;

    GoogleCredentials credentials = loadCredentials();
    FirebaseOptions options =
        FirebaseOptions.builder().setCredentials(credentials).setProjectId(projectId).build();
    FirebaseApp.initializeApp(options);
  }

  private GoogleCredentials loadCredentials() throws IOException {
    // Prefer ADC (GOOGLE_APPLICATION_CREDENTIALS). If not set, try FIREBASE_SERVICE_ACCOUNT_JSON.
    String serviceAccountPath = System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON");
    if (serviceAccountPath != null && !serviceAccountPath.isBlank()) {
      try (InputStream is = new FileInputStream(serviceAccountPath)) {
        return GoogleCredentials.fromStream(is);
      }
    }
    return GoogleCredentials.getApplicationDefault();
  }
}

