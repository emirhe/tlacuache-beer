-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: tlacuache_beer
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `birra`
--

DROP TABLE IF EXISTS `birra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `birra` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(150) NOT NULL,
  `stile_id` int NOT NULL,
  `origine_id` int NOT NULL,
  `produttore_id` int NOT NULL,
  `gradazione` decimal(4,2) DEFAULT NULL,
  `senza_glutine` tinyint(1) DEFAULT NULL,
  `biologico` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_birra_produttore` (`produttore_id`),
  KEY `fk_birra_origine` (`origine_id`),
  KEY `fk_birra_stile` (`stile_id`),
  CONSTRAINT `fk_birra_origine` FOREIGN KEY (`origine_id`) REFERENCES `origine` (`id`),
  CONSTRAINT `fk_birra_produttore` FOREIGN KEY (`produttore_id`) REFERENCES `produttore` (`id`),
  CONSTRAINT `fk_birra_stile` FOREIGN KEY (`stile_id`) REFERENCES `stile` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `birra`
--

LOCK TABLES `birra` WRITE;
/*!40000 ALTER TABLE `birra` DISABLE KEYS */;
INSERT INTO `birra` VALUES (1,'Tlacuache IPA',1,1,1,6.50,0,1),(2,'Tlacuache Stout',2,1,1,5.80,1,0),(3,'Colima Pils',3,1,2,4.50,0,0),(4,'Sierra Nevada Pale Ale',4,2,3,5.60,0,1),(5,'BrewDog Hazy',1,3,4,5.00,0,0),(6,'Baladin Isaac',5,4,5,5.00,0,1),(7,'BrewDog Gluten Free IPA',1,3,4,4.70,1,0),(8,'Tlacuache Pale Ale',4,1,1,5.20,0,0);
/*!40000 ALTER TABLE `birra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `formato`
--

DROP TABLE IF EXISTS `formato`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `formato` (
  `id` int NOT NULL AUTO_INCREMENT,
  `codice` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `codice` (`codice`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formato`
--

LOCK TABLES `formato` WRITE;
/*!40000 ALTER TABLE `formato` DISABLE KEYS */;
INSERT INTO `formato` VALUES (1,'bottiglia'),(3,'fusto'),(2,'lattina');
/*!40000 ALTER TABLE `formato` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `origine`
--

DROP TABLE IF EXISTS `origine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `origine` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `origine`
--

LOCK TABLES `origine` WRITE;
/*!40000 ALTER TABLE `origine` DISABLE KEYS */;
INSERT INTO `origine` VALUES (1,'Messico'),(2,'USA'),(3,'Regno Unito'),(4,'Italia'),(5,'Germania'),(6,'Belgio'),(7,'Rep. Ceca');
/*!40000 ALTER TABLE `origine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodotto`
--

DROP TABLE IF EXISTS `prodotto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prodotto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `birra_id` int NOT NULL,
  `sku` varchar(70) DEFAULT NULL,
  `formato_id` int NOT NULL,
  `volume_ml` int unsigned NOT NULL,
  `pack_size` int unsigned NOT NULL DEFAULT '1',
  `prezzo` decimal(10,2) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `stock` int unsigned NOT NULL DEFAULT '0',
  `url_img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku` (`sku`),
  KEY `fk_prodotto_birra` (`birra_id`),
  KEY `fk_prodotto_formato` (`formato_id`),
  CONSTRAINT `fk_prodotto_birra` FOREIGN KEY (`birra_id`) REFERENCES `birra` (`id`),
  CONSTRAINT `fk_prodotto_formato` FOREIGN KEY (`formato_id`) REFERENCES `formato` (`id`),
  CONSTRAINT `chk_prodotto_pack` CHECK ((`pack_size` in (1,4,6,12,24)))
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodotto`
--

LOCK TABLES `prodotto` WRITE;
/*!40000 ALTER TABLE `prodotto` DISABLE KEYS */;
INSERT INTO `prodotto` VALUES (1,1,'TLA-IPA-330-BOT-P1',1,330,6,59.90,1,120,NULL),(2,1,'TLA-IPA-330-CAN-P6',2,330,6,23.00,1,45,NULL),(3,2,'TLA-STO-500-BOT-P1',1,500,1,12.00,0,60,NULL),(4,2,'TLA-STO-20000-FUS-P1',3,20000,1,200.00,1,5,NULL),(5,3,'COL-PIL-355-CAN-P6',2,355,6,299.00,1,80,NULL),(6,3,'COL-PIL-355-CAN-P12',2,355,12,559.00,1,40,NULL),(7,4,'SNE-PAL-355-BOT-P6',1,355,6,489.00,1,25,NULL),(8,4,'SNE-PAL-355-CAN-P12',2,355,12,929.00,1,12,NULL);
/*!40000 ALTER TABLE `prodotto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produttore`
--

DROP TABLE IF EXISTS `produttore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produttore` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(150) NOT NULL,
  `indirizzo` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produttore`
--

LOCK TABLES `produttore` WRITE;
/*!40000 ALTER TABLE `produttore` DISABLE KEYS */;
INSERT INTO `produttore` VALUES (1,'Tlacuache Brewing','Oaxaca, MX',NULL),(2,'Cervecería de Colima','Colima, MX',NULL),(3,'Sierra Nevada Brewing','Chico, CA, USA',NULL),(4,'BrewDog','Ellon, UK',NULL),(5,'Baladin','Piozzo, IT',NULL);
/*!40000 ALTER TABLE `produttore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stile`
--

DROP TABLE IF EXISTS `stile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stile` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(80) NOT NULL,
  `descrizione` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stile`
--

LOCK TABLES `stile` WRITE;
/*!40000 ALTER TABLE `stile` DISABLE KEYS */;
INSERT INTO `stile` VALUES (1,'IPA','India Pale Ale, luppolata e aromatica'),(2,'Stout','Scura, tostada'),(3,'Pilsner','Rubia, lager, amara limpia'),(4,'Pale Ale','Maltosa y cítrica'),(5,'Witbier','Trigo belga, especiada'),(6,'Lager','Fermentazione bassa, limpia');
/*!40000 ALTER TABLE `stile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'tlacuache_beer'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-19 16:56:25
