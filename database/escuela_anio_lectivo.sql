

CREATE TABLE `anio_lectivo` (
  `id_anio` int NOT NULL AUTO_INCREMENT,
  `anio` smallint NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `estado` varchar(20) DEFAULT 'INACTIVO',
  PRIMARY KEY (`id_anio`),
  UNIQUE KEY `anio` (`anio`),
  CONSTRAINT `anio_lectivo_chk_1` CHECK ((`estado` in (_utf8mb4'ACTIVO',_utf8mb4'INACTIVO',_utf8mb4'FINALIZADO')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
