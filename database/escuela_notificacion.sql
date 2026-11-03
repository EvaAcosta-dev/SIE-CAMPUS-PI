CREATE TABLE IF NOT EXISTS notificaciones (
    id_notificacion INT NOT NULL AUTO_INCREMENT,
    id_usuario_destino INT NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_envio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    leido TINYINT(1) NOT NULL DEFAULT 0,
    tipo_evento ENUM('CONVIVENCIA', 'ASISTENCIA', 'ACADEMICO', 'CIRCULAR') NOT NULL,
    PRIMARY KEY (id_notificacion),
    KEY idx_notificaciones_destino_leido (id_usuario_destino, leido, fecha_envio)
);
