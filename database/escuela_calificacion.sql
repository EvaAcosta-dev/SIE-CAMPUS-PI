--
-- Table structure for table `calificacion`
-- Reemplaza el esquema heredado (que usaba id_matricula/id_periodo) por el del módulo de Notas:
-- CalificacionDao inserta (id_estudiante, id_materia, id_curso, nota, periodo, anio_lectivo, observacion).
--

DROP TABLE IF EXISTS `calificacion`;

CREATE TABLE `calificacion` (
  `id_calificacion` int NOT NULL AUTO_INCREMENT,
  `id_estudiante` int NOT NULL,
  `id_materia` int NOT NULL,
  `id_curso` int NOT NULL,
  `nota` decimal(3,2) NOT NULL,
  `periodo` varchar(30) NOT NULL,
  `anio_lectivo` int NOT NULL,
  `observacion` text,
  `fechaRegistro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_calificacion`),
  KEY `idx_cal_estudiante` (`id_estudiante`),
  KEY `idx_cal_materia` (`id_materia`),
  KEY `idx_cal_curso` (`id_curso`),
  CONSTRAINT `calificacion_chk_1` CHECK (((`nota` >= 0) and (`nota` <= 5)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;