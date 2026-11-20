# AkumaTech — Backend

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-27-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)

Backend REST desarrollado con Spring Boot para la tienda gamer **AkumaTech**. La interfaz web corresponde al proyecto [AkumaTech React](https://github.com/rogodoyr/Akuma_tech_React), que usa este backend para migrar la gestión de usuarios desde `localStorage` hacia una API persistente.

## Estado actual

La primera versión implementada cubre la autenticación de usuarios:

- Registro de usuarios.
- Inicio de sesión con verificación de contraseña.
- Generación de tokens JWT.
- Eliminación lógica mediante el campo `active`.
- Persistencia de usuarios en PostgreSQL.
- Migración inicial de la tabla `users` con Flyway.
- Validación de correos y campos obligatorios.

## Tecnologías

| Capa | Tecnología |
|---|---|
| API | Spring Boot Web MVC |
| Seguridad | Spring Security + BCrypt |
| Autenticación | JSON Web Token (JJWT 0.12.6) |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | PostgreSQL |
| Migraciones | Flyway |
| Validación | Jakarta Validation |
| Build | Gradle |
| Lenguaje | Java 27 |

## Arquitectura

```text
src/main/java/com/akuma/tech/
├── TechApplication.java
└── auth/
    ├── controller/    # Endpoints REST
    ├── dto/           # Request y response DTO
    ├── model/         # Entidad User
    ├── repository/    # Acceso a datos JPA
    ├── service/       # Lógica de autenticación y JWT
    └── config/        # Seguridad y codificación BCrypt
```

## Requisitos

- JDK 27.
- Gradle Wrapper incluido en el proyecto.
- PostgreSQL 16 o compatible.
- Git.

No es necesario instalar Gradle globalmente: el wrapper se ejecuta desde `gradlew.bat` en Windows o `./gradlew` en Linux/macOS.

## Configuración de PostgreSQL

La configuración predeterminada usa:

| Parámetro | Valor |
|---|---|
| Host | `localhost` |
| Puerto | `5435` |
| Base de datos | `authdb` |
| Usuario | `authuser` |
| Contraseña | `authpass` |

Ejemplo para crear la instancia con Docker:

```bash
docker run --name akuma-postgres ^
  -e POSTGRES_DB=authdb ^
  -e POSTGRES_USER=authuser ^
  -e POSTGRES_PASSWORD=authpass ^
  -p 5435:5432 ^
  -d postgres:16
```

En PowerShell, los saltos de línea deben usar el carácter `` ` ``:

```powershell
docker run --name akuma-postgres `
  -e POSTGRES_DB=authdb `
  -e POSTGRES_USER=authuser `
  -e POSTGRES_PASSWORD=authpass `
  -p 5435:5432 `
  -d postgres:16
```

Los parámetros pueden cambiarse en `src/main/resources/application.properties`.

> La configuración incluye el puerto `7778`, el secreto JWT y las credenciales de conexión. En un entorno real, estas credenciales deben cargarse mediante variables de entorno o secretos y no deben quedar en el repositorio.

## Ejecución

Desde la raíz del proyecto:

```bash
# Windows
.\gradlew.bat bootRun

# Linux/macOS
./gradlew bootRun
```

La API quedará disponible en:

```text
http://localhost:7778
```

También puede generarse y ejecutarse el JAR:

```bash
.\gradlew.bat build
java -jar build/libs/tech-0.0.1-SNAPSHOT.jar
```

## API REST

Todos los endpoints forman parte del prefijo `/api/auth`.

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/auth/register` | Registra un usuario y devuelve un JWT |
| `POST` | `/api/auth/login` | Inicia sesión y devuelve un JWT |
| `DELETE` | `/api/auth/delete/{email}` | Desactiva lógicamente un usuario |

### Registro

```http
POST http://localhost:7778/api/auth/register
Content-Type: application/json

{
  "email": "usuario@duocuc.cl",
  "password": "usuario123"
}
```

Respuesta exitosa:

```json
{
  "token": "<JWT>"
}
```

El correo debe tener formato válido y la contraseña no puede estar vacía.

### Inicio de sesión

```http
POST http://localhost:7778/api/auth/login
Content-Type: application/json

{
  "email": "usuario@duocuc.cl",
  "password": "usuario123"
}
```

Respuesta exitosa:

```json
{
  "token": "<JWT>"
}
```

### Eliminación lógica

```http
DELETE http://localhost:7778/api/auth/delete/usuario@duocuc.cl
```

La respuesta exitosa no incluye cuerpo. El usuario conserva su registro en la base de datos, pero queda marcado como inactivo y no podrá iniciar sesión.

### Códigos HTTP relevantes

| Código | Situación |
|---|---|
| `200 OK` | Login o eliminación completados |
| `201` | Registro completado por defecto si se usa el controlador actual |
| `400 Bad Request` | Email o contraseña inválidos |
| `401 Unauthorized` | Credenciales incorrectas |
| `404 Not Found` | Usuario no encontrado al eliminar |
| `409 Conflict` | El correo ya está registrado |

> El controlador devuelve actualmente `200 OK` para el registro; puede ajustarse a `201 Created` como mejora futura.

## Modelo de datos

Entidad `users`:

| Campo | Tipo | Restricción |
|---|---|---|
| `id` | Long | Clave primaria autoincremental |
| `username` | String | Único y obligatorio |
| `email` | String | Único y obligatorio |
| `password` | String | Contraseña cifrada con BCrypt |
| `active` | Boolean | Activo por defecto |

Hibernate usa `ddl-auto=update` y Flyway tiene habilitada la ubicación `classpath:db/migration`.

## Pruebas

Ejecutar todas las pruebas:

```bash
# Windows
.\gradlew.bat test

# Linux/macOS
./gradlew test
```

El reporte HTML se genera en:

```text
build/reports/tests/test/index.html
```

## Conexión con el frontend

El frontend [AkumaTech React](https://github.com/rogodoyr/Akuma_tech_React) usa React, Vite y el contexto `SesionContext` para registrar e iniciar sesión.

Para una integración con este backend, el frontend debe enviar las solicitudes a `http://localhost:7778/api/auth` y guardar el JWT recibido. Una propuesta típica es:

```text
POST /api/auth/register
POST /api/auth/login
Authorization: Bearer <JWT>
```

Como Spring Security está configurado actualmente con `permitAll`, todos los endpoints son públicos. Antes de exponer el backend, debe agregarse un filtro JWT y reglas de autorización para rutas administrativas y protegidas.

## Próximos pasos

- Integrar el JWT en las rutas protegidas.
- Agregar filtros de Spring Security.
- Implementar refresh tokens.
- Migrar los productos, carrito y pedidos desde `localStorage`.
- Agregar roles para usuarios y administradores.
- Agregar pruebas de integración con PostgreSQL o Testcontainers.
- Crear configuración por perfiles (`dev`, `test`, `prod`) y usar variables de entorno.

## Autoría

Proyecto académico Fullstack II — Duoc UC.
