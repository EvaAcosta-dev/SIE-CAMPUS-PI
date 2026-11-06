--
-- Table structure for table `asignacion_docente`
-- Reemplaza el esquema heredado `doc_materia_curso` (sin anio_lectivo/estado/fecha_asignacion) por el del módulo de Asignación Docente:
-- AsignacionDocenteDao inserta (id_docente, id_materia, id_curso, anio_lectivo, estado).
--

DROP TABLE IF EXISTS `asignacion_docente`;

CREATE TABLE `asignacion_docente` (
  `id_asignacion` int NOT NULL AUTO_INCREMENT,
  `id_docente` int NOT NULL,
  `id_materia` int NOT NULL,
  `id_curso` int NOT NULL,
  `anio_lectivo` int NOT NULL,
  `estado` varchar(20) NOT NULL DEFAULT 'ACTIVA',
  `fecha_asignacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_asignacion`),
  KEY `idx_asig_docente` (`id_docente`),
  KEY `idx_asig_materia` (`id_materia`),
  KEY `idx_asig_curso` (`id_curso`),
  CONSTRAINT `asignacion_docente_ibfk_1` FOREIGN KEY (`id_docente`) REFERENCES `docente` (`id_docente`),
  CONSTRAINT `asignacion_docente_ibfk_2` FOREIGN KEY (`id_materia`) REFERENCES `materia` (`id_materia`),
  CONSTRAINT `asignacion_docente_ibfk_3` FOREIGN KEY (`id_curso`) REFERENCES `curso` (`id_curso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;