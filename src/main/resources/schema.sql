-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: ecommerce
-- ------------------------------------------------------
-- Server version	8.0.33

DROP TABLE IF EXISTS `custom_user`;
DROP TABLE IF EXISTS `purchase_order`;
DROP TABLE IF EXISTS `product_category`;
DROP TABLE IF EXISTS `sales_item`;
DROP TABLE IF EXISTS `wishlist`;
DROP TABLE IF EXISTS `account`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `discount`;
DROP TABLE IF EXISTS `product`;

CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `discount` (
  `id` int NOT NULL AUTO_INCREMENT,
  `discount_code` varchar(255) DEFAULT NULL,
  `discount_percent` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `custom_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `discount_code` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('USER','ADMIN') DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `reset_expiry_time` datetime(6) DEFAULT NULL,
  `reset_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` int NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_admin` bit(1) NOT NULL,
  `is_user` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `discount_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp8e8jr7tkvav625g52xysbfpc` (`discount_id`),
  CONSTRAINT `FKp8e8jr7tkvav625g52xysbfpc` FOREIGN KEY (`discount_id`) REFERENCES `discount` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `date_added` bigint NOT NULL,
  `details` TEXT DEFAULT NULL,
  `image_location` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `stock` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `product_category` (
  `product_id` int NOT NULL,
  `category_id` int NOT NULL,
  PRIMARY KEY (`product_id`,`category_id`),
  KEY `FKkud35ls1d40wpjb5htpp14q4e` (`category_id`),
  CONSTRAINT `FK2k3smhbruedlcrvu6clued06x` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKkud35ls1d40wpjb5htpp14q4e` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `account_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6qpfcwhehfb14es2avjemvpwl` (`account_id`),
  KEY `FKorudvkngcr1iy3dkrnfqxgy6n` (`product_id`),
  CONSTRAINT `FK6qpfcwhehfb14es2avjemvpwl` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FKorudvkngcr1iy3dkrnfqxgy6n` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT account_product UNIQUE (account_id, product_id)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quantity_sold` int NOT NULL,
  `time_recorded` bigint NOT NULL,
  `total_price` double NOT NULL,
  `account_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl2tp2yrutm8pkim0bxvtgf2mu` (`account_id`),
  KEY `FK4nk73i69swn51ci60rey5t96g` (`product_id`),
  CONSTRAINT `FK4nk73i69swn51ci60rey5t96g` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKl2tp2yrutm8pkim0bxvtgf2mu` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `wishlist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `price_on_add` double NOT NULL,
  `account_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs4p1y2q4jbqeexffug2bqihtt` (`account_id`),
  KEY `FKqchevbfw5wq0f4uqacns02rp7` (`product_id`),
  CONSTRAINT `FKqchevbfw5wq0f4uqacns02rp7` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKs4p1y2q4jbqeexffug2bqihtt` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT account_product UNIQUE (account_id, product_id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE category AUTO_INCREMENT=0;
ALTER TABLE custom_user AUTO_INCREMENT=0;
ALTER TABLE purchase_order AUTO_INCREMENT=0;
ALTER TABLE product_category AUTO_INCREMENT=0;
ALTER TABLE sales_item AUTO_INCREMENT=0;
ALTER TABLE wishlist AUTO_INCREMENT=0;
ALTER TABLE account AUTO_INCREMENT=0;
ALTER TABLE discount AUTO_INCREMENT=0;
ALTER TABLE product AUTO_INCREMENT=0;