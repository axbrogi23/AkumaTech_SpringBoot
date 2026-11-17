# AkumaTech Backend

API REST en Spring Boot 3.4, Java 17, H2 y JWT.

## Endpoints principales

- `POST /api/auth/register` — crea una cuenta de usuario.
- `POST /api/auth/login` — devuelve un JWT.
- `GET /api/auth/me` — devuelve la sesión actual (`Authorization: Bearer <token>`).
- `GET /api/products` — catálogo público.
- `POST|PUT|DELETE /api/admin/products` — CRUD protegido con rol `ADMIN`.

Usuario administrador inicial: `admin@gmail.com` / `admin123`.

La base H2 se guarda en `backend/data/akumatech`. Para producción se debe cambiar
`JWT_SECRET`, la base de datos y la contraseña inicial.
