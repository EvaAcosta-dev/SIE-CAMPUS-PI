
--
-- Table structure for table `rel_padre_estudiante`
--

CREATE TABLE `rel_padre_estudiante` (
  `id_relacion_padre_estudiante` int NOT NULL AUTO_INCREMENT,
  `id_padre` int NOT NULL,
  `id_estudiante` int NOT NULL,
  `parentesco` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id_relacion_padre_estudiante`),
  KEY `id_padre` (`id_padre`),
  KEY `id_estudiante` (`id_estudiante`),
  CONSTRAINT `rel_padre_estudiante_ibfk_1` FOREIGN KEY (`id_padre`) REFERENCES `padre` (`id_padre`),
  CONSTRAINT `rel_padre_estudiante_ibfk_2` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiante` (`id_estudiante`),
  CONSTRAINT `rel_padre_estudiante_chk_1` CHECK ((`parentesco` in (_utf8mb4'padre',_utf8mb4'madre',_utf8mb4'acudiente')))
) ;


--
-- Dumping data for table `rel_padre_estudiante`
--
