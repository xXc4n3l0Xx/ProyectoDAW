CREATE TABLE estado (
    id_estado INT PRIMARY KEY,
    visible BOOLEAN DEFAULT TRUE
);

INSERT INTO estado (id_estado, visible) VALUES (0, FALSE);
INSERT INTO estado (id_estado, visible) VALUES (1, TRUE);

CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    nombre VARCHAR(50),
    correo VARCHAR(50),
    contrasena VARCHAR(20),
    avatar VARCHAR(500),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_estado INTEGER DEFAULT 1,
    FOREIGN KEY (id_estado) REFERENCES estado(id_estado)
);

CREATE TABLE forum_thread (
    id_thread SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL,
    thread_title VARCHAR(50) NOT NULL,
    thread_created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_estado INTEGER DEFAULT 1,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_estado) REFERENCES estado(id_estado)
);

CREATE TABLE post (
    id_post SERIAL PRIMARY KEY,
    id_thread INTEGER NOT NULL,
    id_usuario INTEGER NOT NULL,
    post_content TEXT NOT NULL,
    post_created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_estado INTEGER DEFAULT 1,
    FOREIGN KEY (id_thread) REFERENCES forum_thread(id_thread),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_estado) REFERENCES estado(id_estado)
);

CREATE TYPE enemy_type_enum AS ENUM ('esqueleto', 'mariposa', 'calcio', 'lanzador');

CREATE TABLE enemies (
    id SERIAL PRIMARY KEY,
    health INT NOT NULL,
    move_speed FLOAT NOT NULL,
    is_dead BOOLEAN DEFAULT FALSE,
    enemy_type enemy_type_enum NOT NULL,
    id_estado INTEGER DEFAULT 1,
    FOREIGN KEY (id_estado) REFERENCES estado(id_estado)
);

CREATE TABLE mariposa (
    id INTEGER PRIMARY KEY,
    top_offset FLOAT NOT NULL,
    bottom_offset FLOAT NOT NULL,
    pause_time FLOAT NOT NULL,
    FOREIGN KEY (id) REFERENCES enemies(id) ON DELETE CASCADE
);

CREATE TABLE calcio (
    id INTEGER PRIMARY KEY,
    pause_duration FLOAT NOT NULL,
    charge_distance FLOAT NOT NULL,
    FOREIGN KEY (id) REFERENCES enemies(id) ON DELETE CASCADE
);

CREATE TABLE lanzador (
    id INTEGER PRIMARY KEY,
    throw_force FLOAT NOT NULL,
    FOREIGN KEY (id) REFERENCES enemies(id) ON DELETE CASCADE
);

CREATE TABLE esqueleto (
    id INTEGER PRIMARY KEY,
    attack_range FLOAT NOT NULL,
    attack_duration FLOAT NOT NULL,
    hit_delay FLOAT NOT NULL,
    FOREIGN KEY (id) REFERENCES enemies(id) ON DELETE CASCADE
);

CREATE TABLE base_player (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    speed FLOAT DEFAULT 200.0,
    jump_force FLOAT DEFAULT -400.0,
    gravity FLOAT DEFAULT 980.0,
    air_acceleration FLOAT DEFAULT 0.6,
    health INT DEFAULT 100,
    attack_damage INT DEFAULT 10,
    throw_damage INT DEFAULT 15,
    position_x FLOAT DEFAULT 0.0,
    position_y FLOAT DEFAULT 0.0,
    is_dead BOOLEAN DEFAULT FALSE,
    id_estado INTEGER DEFAULT 1,
    FOREIGN KEY (id_estado) REFERENCES estado(id_estado)
);

CREATE TABLE mago (
    id INTEGER PRIMARY KEY,
    mana INT DEFAULT 100,
    FOREIGN KEY (id) REFERENCES base_player(id) ON DELETE CASCADE
);

CREATE TABLE espadachin (
    id INTEGER PRIMARY KEY,
    energy INT DEFAULT 100,
    FOREIGN KEY (id) REFERENCES base_player(id) ON DELETE CASCADE
);