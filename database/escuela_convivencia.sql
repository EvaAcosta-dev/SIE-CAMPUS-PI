--
-- Table structure for table `convivencia`
-- Creada a partir del módulo de Convivencia (ConvivenciaDao / ConvivenciaController / ConvivenciaPanel).
--

DROP TABLE IF EXISTS `convivencia`;

CREATE TABLE `convivencia` (
  `id_convivencia` int NOT NULL AUTO_INCREMENT,
  `id_estudiante` int NOT NULL,
  `id_docente` int NOT NULL,
  `fecha_reporte` date NOT NULL,
  `tipo_falta` varchar(20) NOT NULL,
  `observacion` text,
  `medida_disciplinaria` text,
  `estado` varchar(20) NOT NULL DEFAULT 'PENDIENTE',
  `fechaRegistro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_convivencia`),
  KEY `idx_conv_estudiante` (`id_estudiante`),
  KEY `idx_conv_docente` (`id_docente`),
  CONSTRAINT `convivencia_ibfk_1` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiante` (`id_estudiante`),
  CONSTRAINT `convivencia_ibfk_2` FOREIGN KEY (`id_docente`) REFERENCES `docente` (`id_docente`),
  CONSTRAINT `convivencia_chk_1` CHECK ((`tipo_falta` in ('LEVE','GRAVE','GRAVISIMA'))),
  CONSTRAINT `convivencia_chk_2` CHECK ((`estado` in ('PENDIENTE','EN_SEGUIMIENTO','RESUELTO','ARCHIVADO')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;