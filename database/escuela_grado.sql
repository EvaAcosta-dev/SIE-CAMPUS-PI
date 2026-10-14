
DROP TABLE IF EXISTS `grado`;

CREATE TABLE `grado` (
  `id_grado` int NOT NULL AUTO_INCREMENT,
  `numero_grado` smallint NOT NULL,
  `nombre_visible` varchar(50) NOT NULL,
  `id_nivel` int NOT NULL,
  PRIMARY KEY (`id_grado`),
  UNIQUE KEY `numero_grado` (`numero_grado`),
  KEY `fk_grado_nivel` (`id_nivel`),
  CONSTRAINT `fk_grado_nivel` FOREIGN KEY (`id_nivel`) REFERENCES `nivel` (`id_nivel`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Dumping data for table `grado`
--

INSERT INTO `grado` VALUES (1,-1,'Pre-Jardin',1),(2,0,'Transicion',1);

