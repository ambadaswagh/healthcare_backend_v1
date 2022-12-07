DROP TABLE IF EXISTS `content`;

CREATE TABLE `content` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `content` longblob NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` tinyint(4) NOT NULL,
  `agency_id` int(11) unsigned DEFAULT NULL,
  `company_id` int(11) unsigned DEFAULT NULL,
  `content_type` int(11) DEFAULT NULL,
  `admin_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_content_agency_idx` (`agency_id`),
  KEY `fk_content_company_idx` (`company_id`),
  CONSTRAINT `fk_content_agency` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_content_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

INSERT INTO `content_type` (`id`,`name`,`status`) VALUES (3,'Contact Us',1);
INSERT INTO `content_type` (`id`,`name`,`status`) VALUES (4,'Faqs',1);
INSERT INTO `content_type` (`id`,`name`,`status`) VALUES (5,'About Us',1);
INSERT INTO `content_type` (`id`,`name`,`status`) VALUES (6,'Disclaimer',1);
INSERT INTO `content_type` (`id`,`name`,`status`) VALUES (7,'Announcement',1);
INSERT INTO `content_type` (`id`,`name`,`status`) VALUES (8,'Senior Registration Instruction',1);