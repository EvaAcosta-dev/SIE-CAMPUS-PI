
--
-- Table structure for table `boletines`
--

CREATE TABLE `boletines` (
  `id_boletin` int NOT NULL AUTO_INCREMENT,
  `id_matricula` int NOT NULL,
  `id_periodo` int NOT NULL,
  `fecha_generacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `ruta_archivo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_boletin`),
  KEY `id_matricula` (`id_matricula`),
  KEY `id_periodo` (`id_periodo`),
  CONSTRAINT `boletines_ibfk_1` FOREIGN KEY (`id_matricula`) REFERENCES `matricula` (`id_matricula`),
  CONSTRAINT `boletines_ibfk_2` FOREIGN KEY (`id_periodo`) REFERENCES `periodos` (`id_periodo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
