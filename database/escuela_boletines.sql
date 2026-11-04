--
-- Table structure for table `boletin`
-- Reemplaza el esquema heredado `boletines` (id_matricula/id_periodo/ruta_archivo) por el del módulo de Boletines:
-- BoletinDao inserta (id_estudiante, id_curso, anio_lectivo, periodo, promedio_general, aprobadas, reprobadas, estado_boletin).
--

DROP TABLE IF EXISTS `boletin`;

CREATE TABLE `boletin` (
  `id_boletin` int NOT NULL AUTO_INCREMENT,
  `id_estudiante` int NOT NULL,
  `id_curso` int NOT NULL,
  `anio_lectivo` int NOT NULL,
  `periodo` varchar(30) NOT NULL,
  `promedio_general` decimal(4,2) NOT NULL DEFAULT 0,
  `aprobadas` int NOT NULL DEFAULT 0,
  `reprobadas` int NOT NULL DEFAULT 0,
  `estado_boletin` varchar(20) NOT NULL DEFAULT 'GENERADO',
  `fecha_generacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_boletin`),
  KEY `idx_boletin_estudiante` (`id_estudiante`),
  KEY `idx_boletin_curso` (`id_curso`),
  CONSTRAINT `boletin_ibfk_1` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiante` (`id_estudiante`),
  CONSTRAINT `boletin_ibfk_2` FOREIGN KEY (`id_curso`) REFERENCES `curso` (`id_curso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;