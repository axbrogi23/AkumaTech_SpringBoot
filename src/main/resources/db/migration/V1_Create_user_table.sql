CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índice para optimizar la autenticación y búsquedas por nombre de usuario
CREATE INDEX idx_users_username ON users(username);
-- Índice para optimizar búsquedas por email
CREATE INDEX idx_users_email ON users(email);