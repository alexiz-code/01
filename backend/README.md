## Backend (Spring Boot)

Este backend expone una **API REST** en `http://localhost:8080/api` y persiste en **Firestore (Firebase)**.

### Cómo ejecutar

En PowerShell, dentro de `backend/`:

```bash
.\mvnw.cmd spring-boot:run
```

### Endpoints

- `GET /api/health`
- **Usuarios**
  - `GET /api/users`
  - `GET /api/users/{id}`
  - `POST /api/users`
  - `PUT /api/users/{id}`
  - `DELETE /api/users/{id}`
- **Destinos**
  - `GET /api/destinos`
- **Reservas**
  - `GET /api/reservas`
  - `GET /api/reservas/{id}`
  - `POST /api/reservas`
  - `DELETE /api/reservas/{id}`
- **Contacto**
  - `GET /api/contacto`
  - `GET /api/contacto/{id}`
  - `POST /api/contacto`
  - `DELETE /api/contacto/{id}`

### Firebase / Firestore

- Debes configurar credenciales para Firebase Admin:
  - Opción A (recomendada): variable de entorno `GOOGLE_APPLICATION_CREDENTIALS` apuntando al JSON del Service Account
  - Opción B: variable de entorno `FIREBASE_SERVICE_ACCOUNT_JSON` apuntando al mismo JSON
- Proyecto: `g7-angular-2026`

