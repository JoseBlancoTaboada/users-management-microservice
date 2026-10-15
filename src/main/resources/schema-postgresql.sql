-- =============================================
-- Script de creacion de la tabla de usuarios para PostgreSQL
-- Gestion de Usuarios - Arquitectura Hexagonal
-- Equivale a schema.sql (MySQL): role y status son VARCHAR con CHECK en lugar de ENUM,
-- y updated_at lo actualiza el adaptador (PostgreSQL no tiene ON UPDATE CURRENT_TIMESTAMP).
-- =============================================

CREATE TABLE IF NOT EXISTS users (
    id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL CHECK (role IN ('ADMIN', 'MEMBER', 'REVIEWER')),
    status      VARCHAR(20)  NOT NULL DEFAULT 'PENDING'
                CHECK (status IN ('ACTIVE', 'INACTIVE', 'PENDING', 'BLOCKED')),
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- La aplicacion se conecta directo por JDBC; la API REST de Supabase no debe leer esta tabla.
ALTER TABLE users ENABLE ROW LEVEL SECURITY;

-- Usuario administrador inicial (password: Admin1234!). Cambiarlo despues del primer despliegue.
INSERT INTO users (id, name, email, password, role, status)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'Administrador',
    'admin@example.com',
    '$2a$12$sfwkajsls.fktQb0Ngw5cOiSiV3z4LIwbucSAy4sw2wyNlxbUBt4q',
    'ADMIN',
    'ACTIVE'
)
ON CONFLICT (id) DO NOTHING;
