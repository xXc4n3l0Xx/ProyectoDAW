-- Crear base de datos
CREATE DATABASE ProyectoDAW;

-- Tabla estado (activo/inactivo)
CREATE TABLE estado (
  id_estado SERIAL PRIMARY KEY,
  descripcion VARCHAR(20) NOT NULL
);

-- Tabla enemies
CREATE TABLE enemies (
  id SERIAL PRIMARY KEY,
  health INT NOT NULL,
  move_speed REAL NOT NULL,
  is_dead BOOLEAN DEFAULT FALSE,
  enemy_type VARCHAR(20) NOT NULL CHECK (enemy_type IN ('esqueleto', 'mariposa', 'calcio', 'lanzador')),
  id_estado INT DEFAULT 1,
  FOREIGN KEY (id_estado) REFERENCES estado(id_estado)
);

-- Especializaciones
CREATE TABLE mariposa (
  id INT PRIMARY KEY,
  top_offset REAL NOT NULL,
  bottom_offset REAL NOT NULL,
  pause_time REAL NOT NULL,
  FOREIGN KEY (id) REFERENCES enemies(id)
);

CREATE TABLE calcio (
  id INT PRIMARY KEY,
  pause_duration REAL NOT NULL,
  charge_distance REAL NOT NULL,
  FOREIGN KEY (id) REFERENCES enemies(id)
);

CREATE TABLE lanzador (
  id INT PRIMARY KEY,
  throw_force REAL NOT NULL,
  FOREIGN KEY (id) REFERENCES enemies(id)
);

CREATE TABLE esqueleto (
  id INT PRIMARY KEY,
  attack_range REAL NOT NULL,
  attack_duration REAL NOT NULL,
  hit_delay REAL NOT NULL,
  FOREIGN KEY (id) REFERENCES enemies(id)
);

-- Jugador base
CREATE TABLE base_player (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  tipo VARCHAR(50) NOT NULL,
  speed REAL DEFAULT 200.0,
  jump_force REAL DEFAULT -400.0,
  gravity REAL DEFAULT 980.0,
  air_acceleration REAL DEFAULT 0.6,
  max_health INT DEFAULT 10,
  health INT NOT NULL,
  attack_damage INT DEFAULT 10,
  throw_damage INT DEFAULT 15,
  position_x REAL DEFAULT 0.0,
  position_y REAL DEFAULT 0.0,
  is_dead BOOLEAN DEFAULT FALSE,
  id_estado INT DEFAULT 1,
  FOREIGN KEY (id_estado) REFERENCES estado(id_estado)
);

-- Clases
CREATE TABLE mago (
  id INT PRIMARY KEY,
  mana INT DEFAULT 100,
  FOREIGN KEY (id) REFERENCES base_player(id)
);

CREATE TABLE espadachin (
  id INT PRIMARY KEY,
  energy INT DEFAULT 100,
  FOREIGN KEY (id) REFERENCES base_player(id)
);

-- Roles
CREATE TABLE rol (
  id_rol SERIAL PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Usuarios
CREATE TABLE usuario (
  id_usuario SERIAL PRIMARY KEY,
  nombre VARCHAR(50) UNIQUE,
  correo VARCHAR(50) UNIQUE,
  contrasena VARCHAR(100),
  avatar VARCHAR(500),
  fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  puntuacion INT DEFAULT 0,
  id_estado INT DEFAULT 1,
  id_rol INT DEFAULT 2,
  FOREIGN KEY (id_estado) REFERENCES estado(id_estado),
  FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

-- Hilos del foro
CREATE TABLE forum_thread (
  id_thread SERIAL PRIMARY KEY,
  id_usuario INT NOT NULL,
  thread_title VARCHAR(50) NOT NULL,
  thread_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  id_estado INT DEFAULT 1,
  FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
  FOREIGN KEY (id_estado) REFERENCES estado(id_estado)
);

-- Posts
CREATE TABLE post (
  id_post SERIAL PRIMARY KEY,
  id_thread INT NOT NULL,
  id_usuario INT NOT NULL,
  post_content TEXT NOT NULL,
  post_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  id_estado INT DEFAULT 1,
  FOREIGN KEY (id_thread) REFERENCES forum_thread(id_thread),
  FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
  FOREIGN KEY (id_estado) REFERENCES estado(id_estado)
);

-- Niveles del juego
CREATE TABLE nivel (
  id_nivel SERIAL PRIMARY KEY,
  numero_nivel INT NOT NULL,
  nombre VARCHAR(100),
  ruta_escena VARCHAR(255) NOT NULL,
  ruta_ost VARCHAR(255),
  salud_guardada INT DEFAULT 10,
  puntuacion_guardada INT DEFAULT 0,
  diamantes_requeridos INT DEFAULT 5,
  desbloqueado BOOLEAN DEFAULT FALSE,
  id_estado INT DEFAULT 1,
  FOREIGN KEY (id_estado) REFERENCES estado(id_estado)
);

-- Recolectables
CREATE TABLE recolectable (
  id_recolectable SERIAL PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL,
  valor_puntaje INT DEFAULT 0,
  id_estado INT DEFAULT 1,
  FOREIGN KEY (id_estado) REFERENCES estado(id_estado)
);

-- Relaciones
CREATE TABLE enemigos_nivel (
  id_nivel INT,
  id_enemigo INT,
  pos_x REAL DEFAULT 0.0,
  pos_y REAL DEFAULT 0.0,
  PRIMARY KEY (id_nivel, id_enemigo, pos_x, pos_y),
  FOREIGN KEY (id_nivel) REFERENCES nivel(id_nivel),
  FOREIGN KEY (id_enemigo) REFERENCES enemies(id)
);

CREATE TABLE recolectables_nivel (
  id_nivel INT,
  id_recolectable INT,
  pos_x REAL DEFAULT 0.0,
  pos_y REAL DEFAULT 0.0,
  PRIMARY KEY (id_nivel, id_recolectable, pos_x, pos_y),
  FOREIGN KEY (id_nivel) REFERENCES nivel(id_nivel),
  FOREIGN KEY (id_recolectable) REFERENCES recolectable(id_recolectable)
);

-- Estados
INSERT INTO estado (descripcion) VALUES ('Activo');  -- id_estado = 1
INSERT INTO estado (descripcion) VALUES ('Inactivo'); -- id_estado = 2

-- Roles
INSERT INTO rol (nombre) VALUES ('admin'); -- id_rol = 1
INSERT INTO rol (nombre) VALUES ('usuario'); -- id_rol = 2