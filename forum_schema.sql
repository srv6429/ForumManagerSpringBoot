
DROP SCHEMA IF EXISTS `forum_springboot`;

CREATE SCHEMA  IF NOT EXISTS `forum_springboot` 
	/*!40100 DEFAULT CHARACTER SET utf8 */
;

-- CREATE USER 'springuser'@'localhost' IDENTIFIED BY 'spring123';
-- GRANT ALL PRIVILEGES ON `forum_springboot`.* TO 'springuser'@'localhost' WITH GRANT OPTION;

USE `forum_springboot`;


-- MySQL Distrib 5.7.29, for Linux (x86_64)


-- ----------------------------------------------------------------------------
-- Structure de la table `user`
--

DROP TABLE IF EXISTS `forum_springboot`.`user`;
CREATE TABLE `forum_springboot`.`user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Insertion de données dans la table `user`
--

LOCK TABLES `forum_springboot`.`user` WRITE;
/*!40000 ALTER TABLE `forum_springboot`.`user` DISABLE KEYS */;
INSERT INTO `forum_springboot`.`user` VALUES 
	(1,'admin','$2a$10$ta/lBzZlPBhrqDv5aO9kh.yR5nNY4YkcNjI/VJTb1iJRVoDnNYZ4O','admin@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39',1),
    (2,'zeus','$2a$10$lgDCNYkCVi.1aln81ALVCeHpNrxEXZa27LhanSqMqMxNgwcu4qaTi','zeus@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39',1),
    (3,'hera','$2a$10$CRAnmH41j/Ga6K3j7aTWAesWHf3vkRNBRKX.1xsUZ07mBKdGBJkpK','hera@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39',1),
    (4,'ares','$2a$10$OUDlq4qGbw1AWg5Be4iJI.AgoTjOnkW1dhBhadujzJE3uQuewOstO','ares@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39',1),
    (5,'apollon','$2a$10$64aZwijfuu4rsq2vujWLUepoOpqWTWR5n9K9SM0VroSppthN1JVGa','apollon@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39',1),
	(6,'hades','$2a$10$nLIqJiV9ANE4.bPtKlIwVe2airhljRzv72AFqHmTlF/GBEMfCY0dW','hades@olumpos.org','2019-09-08 03:45:39','2020-02-05 23:08:27',1),
    (7,'hestia','$2a$10$CKQOFgBHOMW7ljwANxotrevRybOAbsNCXRyF9aKKJAhe8gbtmyHTy','hestia@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39',1),
    (8,'athena','$2a$10$Ej24Qb6Nd9DeiYyFDJjg7ORTyegMRsYAp6YVx09z2tL2FK8C5rv8S','athena@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39',1),
    (9,'poseidon','$2a$10$BCdFdtIrE4L6zvGNo7CgneKt9Fwfa2ZGrDuOLHU49CZmBdgqu814G','poseidon@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39',1),
    (10,'aphrodite','$2a$10$aOhNeH82VqT56JDvC9UtyO4CrcMaLuFWksylIcJ7al63MtSntXcHG','aphrodite@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39',1),
    (11,'artemis','$2a$10$UwTA4FyHQu3YVomtU.s.E.oFpez9Mjo0I5uqaBEus.e0hhG2Ndhuy','artemis@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39',1),
    (12,'hephaïstos','$2a$10$S9U1VJ.q/eHsLoPVK6VthuSK1QhT4dgWvk0oTtdVc9SL/DqIdVrti','hephaïstos@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39',1),
    (13,'pegase','$2a$10$44VFlMLa1cbcFDLg08bJn.yBhknALrBD9yRNKqgluwZ/WPp5WUr5W','pegase@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39',1),
    (14,'icare','$2a$10$3mLoaMekQqZjOMHGUmVKU.9QIjbkR6cuYiTGNrUreIQ669Ce3s8va','icare@olumpos.org','2019-09-08 03:45:39','2019-09-08 18:57:57',1),
    (15,'dummy','$2a$10$3GRuyeys6SzdqM0GwhhzDuUqMHQoPATxpzaZTRWuZ.PUerRueYBWi','dummy@olumpos.org','2019-09-08 03:45:39','2019-09-08 03:45:39',1)
;
/*!40000 ALTER TABLE `forum_springboot`.`user` ENABLE KEYS */;
UNLOCK TABLES;

-- ----------------------------------------------------------------------------
-- Structure de la table `role`
--

DROP TABLE IF EXISTS `forum_springboot`.`role`;
CREATE TABLE `forum_springboot`.`role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Insertion de données dans la table `role`
--

LOCK TABLES `forum_springboot`.`role` WRITE;
/*!40000 ALTER TABLE `forum_springboot`.`role` DISABLE KEYS */;
INSERT INTO `role` VALUES 
	(1,'ROLE_ADMIN'),
    (2,'ROLE_USER')
;
/*!40000 ALTER TABLE `forum_springboot`.`role` ENABLE KEYS */;
UNLOCK TABLES;

-- ----------------------------------------------------------------------------
-- Structure de la table `user_role` : table associative qui relie les utilisateurs à leurs rôles
--

DROP TABLE IF EXISTS `forum_springboot`.`user_role`;
CREATE TABLE `forum_springboot`.`user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  KEY `fk_user_role_user` (`user_id`),
  KEY `fk_user_role_role` (`role_id`),
  CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `forum_springboot`.`role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `forum_springboot`.`user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Insertion de données dans la table `user_role`
--

LOCK TABLES `forum_springboot`.`user_role` WRITE;
/*!40000 ALTER TABLE `forum_springboot`.`user_role` DISABLE KEYS */;
INSERT INTO `forum_springboot`.`user_role` VALUES 
	(1,1),
    (1,2),
    (2,1),
    (2,2),
    (3,2),
    (4,2),
    (5,2),
    (6,2),
    (7,2),
    (8,2),
    (9,2),
    (10,2),
    (11,2),
    (12,2),
    (13,2),
    (14,2),
    (15,2)
;
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

-- ----------------------------------------------------------------------------

-- Structure de la table `topic`:
-- Table contenant les topics
--

DROP TABLE IF EXISTS `forum_springboot`.`topic`;
CREATE TABLE `forum_springboot`.`topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `creator_id` int(11) DEFAULT NULL,
  `is_open` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_topic_user1_idx` (`creator_id`),
  CONSTRAINT `fk_topic_user1` FOREIGN KEY (`creator_id`) REFERENCES `forum_springboot`.`user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Insertion de données dans la table `topic`
--


LOCK TABLES `forum_springboot`.`topic` WRITE;
/*!40000 ALTER TABLE `forum_springboot`.`topic` DISABLE KEYS */;
INSERT INTO `topic` 
	(`id`,`title`,`creation_date`,`update_date`,`creator_id`,`is_open`)
	VALUES 
	(1,'La météo','2019-07-08 03:45:40','2020-02-10 19:34:17',3,1),
    (2,'Les élections','2019-09-17 14:22:56','2019-09-17 14:22:56',11,1),
    (3,'Le libre échange USA/Canada/Mexique','2019-10-05 21:19:28','2019-10-05 21:19:28',2,1),
    (4,'Le retour des Nordiques','2019-10-07 12:46:23','2019-10-07 12:46:23',8,1),
    (5,'Les difficultés des media écrits','2019-10-24 16:51:12','2019-10-24 16:51:12',4,1),
    (6,'La déroute des Républicains','2019-11-23 15:08:27','2019-11-23 15:08:27',7,1),
	(7,'Les déboires du Canadien de Montréal','2019-11-25 12:22:16','2020-01-08 20:15:40',10,1),
    (8,'La balance du pouvoir','2020-02-03 23:35:52','2020-02-04 11:31:42',7,1),
    (9,'Les élections états-uniennes','2020-02-08 04:22:52','2020-02-08 04:22:52',5,1)
;

/*!40000 ALTER TABLE `forum_springboot`.`topic` ENABLE KEYS */;
UNLOCK TABLES;



-- ----------------------------------------------------------------------------
-- Structure de la table `post`
--

DROP TABLE IF EXISTS `forum_springboot`.`post`;
CREATE TABLE `forum_springboot`.`post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment` text NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` int(11) DEFAULT NULL,
  `topic_id` int(11) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_post_user_idx` (`user_id`),
  KEY `fk_post_topic1_idx` (`topic_id`),
  CONSTRAINT `fk_post_topic1` FOREIGN KEY (`topic_id`) REFERENCES `forum_springboot`.`topic` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_post_user` FOREIGN KEY (`user_id`) REFERENCES `forum_springboot`.`user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Insertion de données dans la table `post`
--

LOCK TABLES `forum_springboot`.`post` WRITE;
/*!40000 ALTER TABLE `forum_springboot`.`post` DISABLE KEYS */;
INSERT INTO `post` (`id`,`comment`,`creation_date`,`update_date`,`user_id`,`topic_id`,`is_active`)
	VALUES 
	(1,'L\'Europe agonise sous la canicule','2019-07-08 03:45:40','2019-07-08 03:45:40',3,1,1),
    (2,'Paris brûle-t-il?. 40 degrés à Paris! Du jamais vu!','2019-07-09 12:33:49','2019-07-09 12:33:49',8,1,1),
    (3,'Encore des élections cet automne','2019-09-17 14:22:56','2019-09-17 14:22:56',11,2,1),
    (4,'Ceci est un premier commentaire','2019-10-05 21:19:28','2019-10-05 21:19:28',2,3,1),
	(5,'Le retour des Nordiques ne se fera pas dans les prochaines années','2019-10-07 12:46:23','2019-10-07 12:46:23',8,4,1),
    (6,'Les Nordiques ont besoin d\'un nouvel amphithéâtre. La guimauve n\'a pas coûté assez cher','2019-10-08 14:26:31','2019-10-08 14:26:31',11,4,1),
	(7,'Les médias électroniques et les réseaux sociaux semblent avoir tué les media écrits','2019-10-24 16:51:12','2019-10-24 16:51:12',4,5,1),
    (8,'Même les journaux électroniques peinent à survivre sans aide financière des gouvernements','2019-10-26 05:12:21','2019-10-26 05:12:21',8,5,1),
    (9,'Vaut mieux être patient. Le retour des Nordiques ne se fera pas avant celui des Expos ... i.e. jamais','2019-10-29 21:37:52','2019-10-29 21:37:52',9,4,1),
	(10,'Un retour possible... Ne soyons pas si pessimiste','2019-11-04 22:12:03','2019-11-04 22:12:03',2,4,1),
    (11,'Trump divise! Mais les Républicains semblent se ranger derrière leur président ou n\'osent tout simplement pas le défier','2019-11-23 15:08:27','2019-11-23 15:08:27',7,6,1),
	(12,'La coupe de retour à Montréal ? Ce n\'est pas pour demain.','2019-11-25 12:22:16','2019-11-25 12:22:16',10,7,1),
    (13,'La haine envers les démocrates semble l\'emporter par dessus tout. C\'est pour cette raison que les républicains appuient sans réserve leur président','2019-12-05 17:21:04','2019-12-05 17:21:04',12,6,1),
    (14,'Le problème ce sont les filets. Il faut élargir le filet adverse, et rétrécir celui du Canadien','2019-12-13 14:18:45','2019-12-13 14:18:45',9,7,1),
    (15,'Ce sont les blessures qui ont coulé le Canadien cette année. Il faut échanger tous les soigneurs.','2020-01-08 20:15:40','2020-01-08 20:15:40',10,7,1),
	(16,'L\'Australie a connu l\'un des pires \'hivers\' avec la chaleur et les incendies','2020-02-04 05:52:06','2020-02-04 05:52:06',5,1,1),
	(17,'Une démocratie en santé permet à plus d\'un partis de bénéficier de la balance du pouvoir.','2020-02-07 23:35:52','2020-02-07 23:35:52',3,8,1),
    (18,'Bien sûr qu\'un gouvernement minoritaire procure aux partis d\'opposition plus de pouvoir face au gouvernement, mais celui-ci risque d\'être paralysé et marqué par l\'indécision.','2020-02-08 11:31:42','2020-02-08 11:31:42',6,8,1),
    (19,'Les primaires pour les démocrates semblent bien mal s\'amorcer. Le cafouillage dans l\'Iowa ne leur fait pas bonne presse ','2020-02-08 14:22:52','2020-02-08 14:22:52',5,9,1),
    (20,' ... et maintenant les innondations  ','2020-02-10 19:34:17','2020-02-10 19:34:17',9,1,1)

;

/*!40000 ALTER TABLE `forum_springboot`.`post` ENABLE KEYS */;
UNLOCK TABLES;


-- ----------------------------------------------------------------------------
-- Structure de la table `persistent_logins`
--

DROP TABLE IF EXISTS `forum_springboot`.`persistent_logins`;
CREATE TABLE `forum_springboot`.`persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Insertion de données dans la table `persistent_logins`
--

LOCK TABLES `forum_springboot`.`persistent_logins` WRITE;
/*!40000 ALTER TABLE `forum_springboot`.`persistent_logins` DISABLE KEYS */;
/*!40000 ALTER TABLE `forum_springboot`.`persistent_logins` ENABLE KEYS */;
UNLOCK TABLES;

-- ----------------------------------------------------------------------------
-- Structure de la table `contact`
--
/*
DROP TABLE IF EXISTS `forum_springboot`.`contact`;
CREATE TABLE `forum_springboot`.`contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `notes` text NOT NULL,
  `phone` varchar(255) NOT NULL,
  `website` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
*/

--
-- Insertion de données dans la table `contact`
--
/*
LOCK TABLES `forum_springboot`.`contact` WRITE;
/*!40000 ALTER TABLE `forum_springboot`.`contact` DISABLE KEYS */;
/*
/*!40000 ALTER TABLE `forum_springboot`.`contact` ENABLE KEYS */;
-- UNLOCK TABLES;

-- ----------------------------------------------------------------------------
-- Structure de la table `message`
--
/*
DROP TABLE IF EXISTS `forum_springboot`.`message`;
CREATE TABLE `forum_springboot`.`message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

*/
--
-- Insertion de données dans la table `message`
--
/*
LOCK TABLES `forum_springboot`.`message` WRITE;
/*!40000 ALTER TABLE `forum_springboot`.`message` DISABLE KEYS */;
/*
/*!40000 ALTER TABLE `forum_springboot`.`message` ENABLE KEYS */;
-- UNLOCK TABLES;

