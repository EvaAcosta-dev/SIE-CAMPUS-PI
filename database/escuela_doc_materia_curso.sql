
--
-- Table structure for table `doc_materia_curso`
--

CREATE TABLE `doc_materia_curso` (
  `id_asignacion` int NOT NULL AUTO_INCREMENT,
  `id_docente` int NOT NULL,
  `id_materia` int NOT NULL,
  `id_curso` int NOT NULL,
  PRIMARY KEY (`id_asignacion`),
  KEY `id_docente` (`id_docente`),
  KEY `id_materia` (`id_materia`),
  KEY `id_curso` (`id_curso`),
  CONSTRAINT `doc_materia_curso_ibfk_1` FOREIGN KEY (`id_docente`) REFERENCES `docente` (`id_docente`),
  CONSTRAINT `doc_materia_curso_ibfk_2` FOREIGN KEY (`id_materia`) REFERENCES `materia` (`id_materia`),
  CONSTRAINT `doc_materia_curso_ibfk_3` FOREIGN KEY (`id_curso`) REFERENCES `curso` (`id_curso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `doc_materia_curso`
--
