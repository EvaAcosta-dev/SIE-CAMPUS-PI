--
-- Table structure for table `asistencia`
-- Creada a partir del módulo de Asistencia (AsistenciaDao / AsistenciaController / AsistenciaPanel).
--

DROP TABLE IF EXISTS `asistencia`;

CREATE TABLE `asistencia` (
  `id_asistencia` int NOT NULL AUTO_INCREMENT,
  `id_estudiante` int NOT NULL,
  `id_materia` int NOT NULL,
  `id_curso` int NOT NULL,
  `fecha_clase` date NOT NULL,
  `estado` varchar(20) NOT NULL,
  `observacion` varchar(255) DEFAULT NULL,
  `fechaRegistro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_asistencia`),
  KEY `idx_asist_estudiante` (`id_estudiante`),
  KEY `idx_asist_materia` (`id_materia`),
  KEY `idx_asist_curso` (`id_curso`),
  CONSTRAINT `asistencia_ibfk_1` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiante` (`id_estudiante`),
  CONSTRAINT `asistencia_ibfk_2` FOREIGN KEY (`id_materia`) REFERENCES `materia` (`id_materia`),
  CONSTRAINT `asistencia_ibfk_3` FOREIGN KEY (`id_curso`) REFERENCES `curso` (`id_curso`),
  CONSTRAINT `asistencia_chk_1` CHECK ((`estado` in ('PRESENTE','AUSENTE','JUSTIFICADA')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;