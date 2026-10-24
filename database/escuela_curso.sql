
--
-- Table structure for table `curso`
--
CREATE TABLE `curso` (
  `id_curso` int NOT NULL AUTO_INCREMENT,
  `nombre_curso` varchar(50) NOT NULL,
  `id_grado` int NOT NULL,
  `id_anio` int NOT NULL,
  `id_docente_director` int DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `id_jornada` int NOT NULL,
  PRIMARY KEY (`id_curso`),
  UNIQUE KEY `uk_curso_grado_anio` (`nombre_curso`,`id_grado`,`id_anio`),
  KEY `curso_ibfk_1` (`id_docente_director`),
  KEY `fk_curso_anio_lectivo` (`id_anio`),
  KEY `fk_curso_grado` (`id_grado`),
  KEY `fk_curso_jornada` (`id_jornada`),
  CONSTRAINT `curso_ibfk_1` FOREIGN KEY (`id_docente_director`) REFERENCES `docente` (`id_docente`) ON DELETE SET NULL,
  CONSTRAINT `fk_curso_anio_lectivo` FOREIGN KEY (`id_anio`) REFERENCES `anio_lectivo` (`id_anio`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_curso_grado` FOREIGN KEY (`id_grado`) REFERENCES `grado` (`id_grado`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_curso_jornada` FOREIGN KEY (`id_jornada`) REFERENCES `jornada` (`id_jornada`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;