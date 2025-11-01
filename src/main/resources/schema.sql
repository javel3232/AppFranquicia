-- Crear base de datos
CREATE DATABASE IF NOT EXISTS franquicias;
USE franquicias;

-- Tabla franquicias
CREATE TABLE IF NOT EXISTS franquicias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- Tabla sucursales
CREATE TABLE IF NOT EXISTS sucursales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    franquicia_id BIGINT NOT NULL,
    FOREIGN KEY (franquicia_id) REFERENCES franquicias(id)
);

-- Tabla productos
CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    sucursal_id BIGINT NOT NULL,
    FOREIGN KEY (sucursal_id) REFERENCES sucursales(id)
);

-- Datos de prueba
INSERT INTO franquicias (nombre) VALUES ('McDonald''s');
INSERT INTO franquicias (nombre) VALUES ('Subway');

INSERT INTO sucursales (nombre, franquicia_id) VALUES ('McDonald''s Centro', 1);
INSERT INTO sucursales (nombre, franquicia_id) VALUES ('McDonald''s Norte', 1);