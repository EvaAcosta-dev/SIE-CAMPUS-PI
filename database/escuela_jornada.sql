
--
-- Table structure for table `jornada`
--

CREATE TABLE `jornada` (
  `id_jornada` int NOT NULL AUTO_INCREMENT,
  `nombre_jornada` varchar(50) NOT NULL,
  PRIMARY KEY (`id_jornada`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `jornada`
--


INSERT INTO `jornada` VALUES (1,'Mañana'),(2,'Tarde'),(3,'Fin de Semana'),(4,'Nocturna'),(5,'Completa');
