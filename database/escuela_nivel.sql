
DROP TABLE IF EXISTS `nivel`;

CREATE TABLE `nivel` (
  `id_nivel` int NOT NULL AUTO_INCREMENT,
  `nombre_nivel` varchar(50) NOT NULL,
  PRIMARY KEY (`id_nivel`),
  UNIQUE KEY `nombre_nivel` (`nombre_nivel`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nivel`
--

INSERT INTO `nivel` VALUES (2,'Básica Primaria'),(3,'Básica Secundaria'),(4,'Media'),(1,'Preescolar');
