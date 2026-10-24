
--
-- Table structure for table `periodos`
--
CREATE TABLE `periodos` (
  `id_periodo` int NOT NULL AUTO_INCREMENT,
  `nombre_periodo` varchar(30) NOT NULL,
  `id_anio` int NOT NULL,
  `orden` int NOT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 0, -- 0 = Inactivo, 1 = Activo (Carga de notas permitida)
  PRIMARY KEY (`id_periodo`),
  UNIQUE KEY `uq_periodo_anio_orden` (`id_anio`, `orden`),
  KEY `fk_periodos_anio` (`id_anio`),
  
  KEY `idx_periodo_validacion_notas` (`activo`, `fecha_inicio`, `fecha_fin`), 
  
  CONSTRAINT `fk_periodos_anio` FOREIGN KEY (`id_anio`) REFERENCES `anio_lectivo` (`id_anio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


