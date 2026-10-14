-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)

--
-- Table structure for table `rol`
--

 CREATE DATABASE IF NOT EXISTS escuela;
 USE escuela;

CREATE TABLE `rol` (
  `id_rol` int NOT NULL AUTO_INCREMENT,
  `nombre_rol` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_rol`)
) ;

--
--Agrega registros a la tabala roles 
INSERT INTO `rol` VALUES (1,'ADMIN'),(2,'DOCENTE'),(3,'PADRE'),(4,'ESTUDIANTE'),(5,'REGISTRO ACADEMICO');

