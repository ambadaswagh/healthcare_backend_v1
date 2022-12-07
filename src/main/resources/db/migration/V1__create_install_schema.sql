# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.20)
# Database: healthcare_new
# Generation Time: 2018-02-15 16:29:37 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table activity
# ------------------------------------------------------------

DROP TABLE IF EXISTS `activity`;

CREATE TABLE `activity` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `status` int(11) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `instructor_employee_id` int(11) unsigned NOT NULL,
  `time_start` varchar(255) DEFAULT NULL,
  `time_end` varchar(255) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `note` varchar(1000) DEFAULT NULL,
  `agency_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_activity_instructor_employee_id_1_idx` (`instructor_employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table admin
# ------------------------------------------------------------

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) NOT NULL,
  `gender` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `secondary_phone` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `device_address` varchar(255) DEFAULT NULL,
  `remember_token` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '1',
  `role_id` int(11) unsigned NOT NULL,
  `menu_list` varchar(255) DEFAULT NULL COMMENT 'will have default menu lists that this admin could access',
  `profile_photo_id` int(11) unsigned DEFAULT NULL COMMENT 'will refer the file_upload_id in the file_upload table',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `action_list` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_username_admin` (`username`),
  KEY `fk_admin_role1_idx` (`role_id`),
  KEY `fk_profile_photoadmin_idkey2` (`profile_photo_id`),
  CONSTRAINT `fk_admin_role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;

INSERT INTO `admin` (`id`, `username`, `password`, `first_name`, `middle_name`, `last_name`, `gender`, `email`, `phone`, `secondary_phone`, `ip`, `device_address`, `remember_token`, `status`, `role_id`, `menu_list`, `profile_photo_id`, `created_at`, `updated_at`, `action_list`)
VALUES
	(1,'defaultadmin','$2a$10$GX2t9vBLuVpL0A2.J.bLMedc0ZOhMMVt4zPSeYL/R4G0I8GsCtDgK','admin','admin','admin','Male','admin@gmail.com','1111111111','1','192.168.1.1','1','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZWZhdWx0YWRtaW4iLCJhdWRpZW5jZSI6IndlYiIsImNyZWF0ZWQiOjE1MTgxNzEwMzc3MzQsImV4cCI6MTUxODIxNDIzN30.15CViZBGYJH3aYy6_bRB726EEg69581pL34Ad0Kikwj7lO8CVnqyuYgHu7AUFG4iFE4PSWvYEtwS2PZaC-T4Xg',1,1,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33',NULL,'2017-11-07 19:24:09','2018-02-09 23:18:54','1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,18,19,20,21,22,23,24,25,26,27,28,29,30');

/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table admin_agency_company_organization
# ------------------------------------------------------------

DROP TABLE IF EXISTS `admin_agency_company_organization`;

CREATE TABLE `admin_agency_company_organization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) unsigned NOT NULL,
  `agency_id` int(11) unsigned DEFAULT NULL,
  `company_id` int(11) unsigned DEFAULT NULL,
  `organization_id` int(11) unsigned DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `admin_FK_idx` (`admin_id`),
  KEY `agency_FK_idx` (`agency_id`),
  KEY `company_FK_idx` (`company_id`),
  KEY `organization_FK_idx` (`organization_id`),
  CONSTRAINT `admin_FK` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `agency_FK` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `company_FK` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `organization_FK` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `admin_agency_company_organization` WRITE;
/*!40000 ALTER TABLE `admin_agency_company_organization` DISABLE KEYS */;

INSERT INTO `admin_agency_company_organization` (`id`, `admin_id`, `agency_id`, `company_id`, `organization_id`, `created_at`, `updated_at`)
VALUES
	(1,1,NULL,NULL,NULL,'2018-02-02 21:53:15','2018-02-02 21:53:15');

/*!40000 ALTER TABLE `admin_agency_company_organization` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table admin_permission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `admin_permission`;

CREATE TABLE `admin_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `level` int(11) NOT NULL,
  `level_name` varchar(255) NOT NULL,
  `accessible_menu` varchar(2000) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `admin_permission` WRITE;
/*!40000 ALTER TABLE `admin_permission` DISABLE KEYS */;

INSERT INTO `admin_permission` (`id`, `level`, `level_name`, `accessible_menu`, `status`)
VALUES
	(1,1,'super admin','1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33',1),
	(2,2,'sub super admin','1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28',1),
	(3,3,'company admin ','1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28',1),
	(4,4,'sub company admin','1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28',1),
	(5,5,'senior center admin','1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28',1),
	(6,6,'sub senior center admin','1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28',1);

/*!40000 ALTER TABLE `admin_permission` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table admin_post
# ------------------------------------------------------------

DROP TABLE IF EXISTS `admin_post`;

CREATE TABLE `admin_post` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `post_text` varchar(1000) NOT NULL,
  `post_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL,
  `admin_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_admin_post_admin1_idx` (`admin_id`),
  CONSTRAINT `fk_admin_post_admin1` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table admin_setting
# ------------------------------------------------------------

DROP TABLE IF EXISTS `admin_setting`;

CREATE TABLE `admin_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) unsigned NOT NULL,
  `language_code` varchar(255) DEFAULT NULL,
  `theme` varchar(255) DEFAULT NULL,
  `main_white_label` varchar(255) DEFAULT NULL,
  `upper_left_label` varchar(255) DEFAULT NULL,
  `logo_id` int(11) unsigned DEFAULT NULL,
  `auto_check_out_time` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `meals_selected` varchar(255) DEFAULT NULL,
  `shortcuts` varchar(2000) DEFAULT '[]',
  PRIMARY KEY (`id`),
  KEY `admin_setting_FK_idx` (`admin_id`),
  KEY `admin_setting_FK_id1` (`logo_id`),
  CONSTRAINT `admin_logo_fk` FOREIGN KEY (`logo_id`) REFERENCES `file_upload` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `admin_setting_FK` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table admin_theme
# ------------------------------------------------------------

DROP TABLE IF EXISTS `admin_theme`;

CREATE TABLE `admin_theme` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `theme_selected` varchar(45) DEFAULT NULL,
  `user_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_idx` (`user_id`),
  CONSTRAINT `id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table agency
# ------------------------------------------------------------

DROP TABLE IF EXISTS `agency`;

CREATE TABLE `agency` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `license_no` varchar(255) NOT NULL,
  `company_id` int(11) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `tracking_mode` int(11) NOT NULL,
  `contact_person` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `address_one` varchar(255) NOT NULL,
  `address_two` varchar(255) DEFAULT NULL,
  `city` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `zipcode` varchar(255) NOT NULL,
  `timezone` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `holiday` varchar(1000) NOT NULL,
  `fax` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `agency_type_id` int(11) unsigned NOT NULL,
  `status` int(11) NOT NULL,
  `capacity_num` int(11) DEFAULT NULL,
  `minimum_wage` double(16,2) DEFAULT NULL,
  `cctv_url` varchar(1024) DEFAULT NULL,
  `required_hours` int(11) NOT NULL DEFAULT '4',
  PRIMARY KEY (`id`),
  KEY `fk_agency_agency_type1_idx` (`agency_type_id`),
  KEY `fk_agency_company1_idx` (`company_id`),
  CONSTRAINT `fk_agency_agency_type1` FOREIGN KEY (`agency_type_id`) REFERENCES `agency_type` (`id`),
  CONSTRAINT `fk_agency_company1` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table agency_table
# ------------------------------------------------------------

DROP TABLE IF EXISTS `agency_table`;

CREATE TABLE `agency_table` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `agency_id` int(10) unsigned DEFAULT NULL,
  `table_no` varchar(255) DEFAULT NULL COMMENT 'could be like seat A/B/C/D or 1/2/3/4 etc',
  `table_id` int(11) unsigned DEFAULT NULL,
  `table_number_capacity` int(11) unsigned DEFAULT NULL COMMENT 'how many tables does this agency has',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_AGENCY_ID_idx` (`agency_id`),
  KEY `fk_agency_table_agency_table_fk1_idx` (`table_id`),
  CONSTRAINT `fk_agency_table_agency1` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_agency_table_agency_table_fk1` FOREIGN KEY (`table_id`) REFERENCES `tables` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table agency_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `agency_type`;

CREATE TABLE `agency_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `status` int(11) NOT NULL COMMENT 'type status',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `agency_type` WRITE;
/*!40000 ALTER TABLE `agency_type` DISABLE KEYS */;

INSERT INTO `agency_type` (`id`, `name`, `status`)
VALUES
	(1,'Senior Center',1),
	(2,'Homecare Agency',1);

/*!40000 ALTER TABLE `agency_type` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table appointment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `appointment`;

CREATE TABLE `appointment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned DEFAULT NULL,
  `admin_id` int(11) unsigned DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `reason` varchar(512) DEFAULT NULL,
  `appointment_time` datetime DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `need_notification` int(11) DEFAULT NULL COMMENT '0 for no, 1 for yes',
  `organization_id` int(11) unsigned DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_appointment_user_fk_idx` (`user_id`),
  KEY `fk_appointment_organization_fk_idx` (`organization_id`),
  KEY `fk_appointment_admin_fk_idx` (`admin_id`),
  CONSTRAINT `fk_appointment_admin_fk` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_appointment_organization_fk` FOREIGN KEY (`organization_id`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_appointment_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table assessment_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `assessment_user`;

CREATE TABLE `assessment_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `admin_id` int(11) unsigned NOT NULL,
  `review_id` int(11) unsigned DEFAULT NULL,
  `housing_id` int(11) unsigned DEFAULT NULL,
  `health_status_id` int(11) unsigned DEFAULT NULL,
  `nutrition_id` int(11) unsigned DEFAULT NULL,
  `psycho_social_status_id` int(11) unsigned DEFAULT NULL,
  `medicine_taken_id` int(11) unsigned DEFAULT NULL,
  `iadls_id` int(11) unsigned DEFAULT NULL,
  `service_for_client_id` int(11) unsigned DEFAULT NULL,
  `information_support_id` int(11) unsigned DEFAULT NULL,
  `monthly_income_id` int(11) unsigned DEFAULT NULL,
  `benefit_entitlement_id` int(11) unsigned DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_assessment_user_1` (`user_id`),
  KEY `fk_assessment_user_2` (`admin_id`),
  KEY `fk_assessment_user_3` (`review_id`),
  KEY `fk_assessment_user_4` (`housing_id`),
  KEY `fk_assessment_user_5` (`health_status_id`),
  KEY `fk_assessment_user_6` (`nutrition_id`),
  KEY `fk_assessment_user_7` (`psycho_social_status_id`),
  KEY `fk_assessment_user_8` (`medicine_taken_id`),
  KEY `fk_assessment_user_9` (`iadls_id`),
  KEY `fk_assessment_user_10` (`service_for_client_id`),
  KEY `fk_assessment_user_11` (`information_support_id`),
  KEY `fk_assessment_user_12` (`monthly_income_id`),
  KEY `fk_assessment_user_13` (`benefit_entitlement_id`),
  CONSTRAINT `fk_assessment_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_assessment_user_10` FOREIGN KEY (`service_for_client_id`) REFERENCES `service_for_client` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_assessment_user_11` FOREIGN KEY (`information_support_id`) REFERENCES `information_support` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_assessment_user_12` FOREIGN KEY (`monthly_income_id`) REFERENCES `monthly_income` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_assessment_user_13` FOREIGN KEY (`benefit_entitlement_id`) REFERENCES `benefit_entitlement` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_assessment_user_2` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_assessment_user_3` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_assessment_user_4` FOREIGN KEY (`housing_id`) REFERENCES `housing` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_assessment_user_5` FOREIGN KEY (`health_status_id`) REFERENCES `health_status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_assessment_user_6` FOREIGN KEY (`nutrition_id`) REFERENCES `nutrition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_assessment_user_7` FOREIGN KEY (`psycho_social_status_id`) REFERENCES `psycho_social_status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_assessment_user_8` FOREIGN KEY (`medicine_taken_id`) REFERENCES `medicine_taken` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_assessment_user_9` FOREIGN KEY (`iadls_id`) REFERENCES `iadls` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table benefit_entitlement
# ------------------------------------------------------------

DROP TABLE IF EXISTS `benefit_entitlement`;

CREATE TABLE `benefit_entitlement` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `benefit_status_code` varchar(255) DEFAULT NULL COMMENT 'Options: Has the benefit or entitlement, does not have the benefit or entitlement, Maybe eligiable and is willing to pursue benefit or entitlement, refuse to provide informaiton.',
  `social_security_code` varchar(255) DEFAULT NULL,
  `social_security_comment` varchar(255) DEFAULT NULL,
  `ssl_code` varchar(255) DEFAULT NULL,
  `ssl_comment` varchar(255) DEFAULT NULL,
  `railroad_retirement_code` varchar(255) DEFAULT NULL,
  `railroad_retirement_comment` varchar(255) DEFAULT NULL,
  `ssd_code` varchar(255) DEFAULT NULL,
  `ssd_comment` varchar(255) DEFAULT NULL,
  `veteran_benefits_specify_code` varchar(255) DEFAULT NULL,
  `veteran_benefits_specify_comment` varchar(255) DEFAULT NULL,
  `other_income_related_benefits_code` varchar(255) DEFAULT NULL,
  `other_income_related_benefits_comment` varchar(255) DEFAULT NULL,
  `medicaid_num_code` varchar(255) DEFAULT NULL,
  `medicaid_num_comment` varchar(255) DEFAULT NULL,
  `food_stamp_snap_code` varchar(255) DEFAULT NULL,
  `food_stamp_snap_comment` varchar(255) DEFAULT NULL,
  `public_assistance_specify_code` varchar(255) DEFAULT NULL,
  `public_assistance_specify_comment` varchar(255) DEFAULT NULL,
  `other_specify_code` varchar(255) DEFAULT NULL,
  `other_specify_comment` varchar(255) DEFAULT NULL,
  `other_entitlement_code` varchar(255) DEFAULT NULL,
  `other_entitlement_comment` varchar(255) DEFAULT NULL,
  `medicare_num_code` varchar(255) DEFAULT NULL,
  `medicare_num_comment` varchar(255) DEFAULT NULL,
  `qmb_code` varchar(255) DEFAULT NULL,
  `qmb_comment` varchar(255) DEFAULT NULL,
  `slimb_code` varchar(255) DEFAULT NULL,
  `slimb_comment` varchar(255) DEFAULT NULL,
  `epic_code` varchar(255) DEFAULT NULL,
  `epic_comment` varchar(255) DEFAULT NULL,
  `medicare_part_d_code` varchar(255) DEFAULT NULL,
  `medicare_part_d_comment` varchar(255) DEFAULT NULL,
  `medigap_insurance_hmo_specify_code` varchar(255) DEFAULT NULL,
  `medigap_insurance_hmo_specify_comment` varchar(255) DEFAULT NULL,
  `long_term_care_insurance_specify_code` varchar(255) DEFAULT NULL,
  `long_term_care_insurance_specify_comment` varchar(255) DEFAULT NULL,
  `other_health_insurance_specify_code` varchar(255) DEFAULT NULL,
  `other_health_insurance_specify_comment` varchar(255) DEFAULT NULL,
  `scrie_code` varchar(255) DEFAULT NULL,
  `scrie_comment` varchar(255) DEFAULT NULL,
  `section_eight_code` varchar(255) DEFAULT NULL,
  `section_eight_comment` varchar(255) DEFAULT NULL,
  `ittwo_one_four_code` varchar(255) DEFAULT NULL,
  `ittwo_one_four_comment` varchar(255) DEFAULT NULL,
  `veteran_tax_exemption_code` varchar(255) DEFAULT NULL,
  `veteran_tax_exemption_comment` varchar(255) DEFAULT NULL,
  `reverse_mortgage_code` varchar(255) DEFAULT NULL,
  `reverse_mortgage_comment` varchar(255) DEFAULT NULL,
  `real_property_tax_exemption_star_code` varchar(255) DEFAULT NULL,
  `real_property_tax_exemption_star_comment` varchar(255) DEFAULT NULL,
  `heap_code` varchar(255) DEFAULT NULL,
  `heap_comment` varchar(255) DEFAULT NULL,
  `other_housing_related_benefit_code` varchar(255) DEFAULT NULL,
  `other_housing_related_benefit_comment` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `reserved_1` varchar(255) DEFAULT NULL,
  `reserved_2` varchar(255) DEFAULT NULL,
  `reserved_3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table billing
# ------------------------------------------------------------

DROP TABLE IF EXISTS `billing`;

CREATE TABLE `billing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `visit_id` int(11) DEFAULT NULL,
  `trip_id` int(11) DEFAULT NULL,
  `type` tinyint(1) DEFAULT NULL,
  `happened_date` datetime DEFAULT NULL,
  `billing_deadline` datetime DEFAULT NULL,
  `billing_price` double(16,2) DEFAULT NULL,
  `billing_adjustment` double(16,2) DEFAULT NULL,
  `billing_final_price` double(16,2) DEFAULT NULL,
  `billing_date` datetime DEFAULT NULL,
  `billed_by` int(11) DEFAULT NULL,
  `verified_date` datetime DEFAULT NULL,
  `verified_by` int(11) DEFAULT NULL,
  `status` varchar(255) NOT NULL DEFAULT 'Pending',
  `note` varchar(1024) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table business_hours
# ------------------------------------------------------------

DROP TABLE IF EXISTS `business_hours`;

CREATE TABLE `business_hours` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `day_of_week` tinyint(1) NOT NULL,
  `open_time` time NOT NULL,
  `close_time` time DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `time_zone` varchar(45) DEFAULT NULL,
  `resource_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table chat_group
# ------------------------------------------------------------

DROP TABLE IF EXISTS `chat_group`;

CREATE TABLE `chat_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `created_by` int(11) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='store chat group';



# Dump of table chat_section
# ------------------------------------------------------------

DROP TABLE IF EXISTS `chat_section`;

CREATE TABLE `chat_section` (
  `id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `user_ids` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `from_user` int(11) NOT NULL,
  `to_user` int(11) NOT NULL DEFAULT '-1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



# Dump of table company
# ------------------------------------------------------------

DROP TABLE IF EXISTS `company`;

CREATE TABLE `company` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `license_no` varchar(255) NOT NULL,
  `federal_tax` varchar(255) NOT NULL,
  `federal_tax_start` datetime NOT NULL,
  `federal_tax_expire` datetime NOT NULL,
  `federal_tax_status` int(11) NOT NULL,
  `state_tax` varchar(255) NOT NULL,
  `state_tax_start` datetime NOT NULL,
  `state_tax_expire` datetime NOT NULL,
  `state_tax_status` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `fax` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `address_one` varchar(255) NOT NULL,
  `address_two` varchar(255) DEFAULT NULL,
  `worktime_start` time NOT NULL DEFAULT '00:00:00',
  `worktime_end` time NOT NULL DEFAULT '23:59:59',
  `city` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `zipcode` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `type` varchar(255) NOT NULL,
  `days_work` varchar(255) DEFAULT NULL COMMENT 'Mon, Tue, Wed, Thu, Fri, Sat, Sun.',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `timezone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table content
# ------------------------------------------------------------

DROP TABLE IF EXISTS `content`;

CREATE TABLE `content` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` int(10) unsigned DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `content` longblob NOT NULL,
  `page_title` varchar(255) NOT NULL,
  `page_keyword` text NOT NULL,
  `page_description` text NOT NULL,
  `access_key` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` tinyint(4) NOT NULL,
  `agency_id` int(11) unsigned DEFAULT NULL,
  `company_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `fld_accessNo` (`access_key`),
  KEY `fk_parent_key_content1` (`parent_id`),
  KEY `fk_content_agency_idx` (`agency_id`),
  KEY `fk_content_company_idx` (`company_id`),
  CONSTRAINT `fk_content_agency` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_content_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_parent_key_content1` FOREIGN KEY (`parent_id`) REFERENCES `content` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table cronjob_report
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cronjob_report`;

CREATE TABLE `cronjob_report` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `emp_id` int(11) unsigned NOT NULL,
  `job_status` varchar(10) DEFAULT NULL,
  `run_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `error_desc` varchar(1000) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`emp_id`),
  KEY `cron_job_emp_fk` (`emp_id`),
  CONSTRAINT `fk_cronjob_report_employee` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table driver
# ------------------------------------------------------------

DROP TABLE IF EXISTS `driver`;

CREATE TABLE `driver` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_account_id` varchar(255) DEFAULT NULL,
  `agency_id` int(11) NOT NULL,
  `company_id` int(11) NOT NULL,
  `group_id` int(11) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `driver_type` int(1) NOT NULL,
  `base_percent` double(10,2) DEFAULT NULL,
  `driver_percent` double(10,2) DEFAULT NULL,
  `reserve_percent` double(10,2) DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `fleet_num` varchar(255) DEFAULT NULL,
  `phone` varchar(255) NOT NULL,
  `secondary_phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `dob` date NOT NULL DEFAULT '0000-00-00',
  `gender` int(1) NOT NULL,
  `nationality` varchar(255) DEFAULT NULL,
  `driving_experience` int(11) DEFAULT NULL,
  `verification_code` varchar(255) DEFAULT NULL,
  `profile_photo` varchar(255) DEFAULT NULL,
  `signature` varchar(255) DEFAULT NULL,
  `driver_license` varchar(255) DEFAULT NULL,
  `driver_license_num` varchar(255) DEFAULT NULL,
  `driver_license_class` varchar(255) DEFAULT NULL,
  `driver_license_state` varchar(255) DEFAULT NULL,
  `driver_license_start` date NOT NULL,
  `driver_license_expire` date NOT NULL,
  `driver_license_status` int(11) DEFAULT NULL,
  `driver_tlc_fhv_license` varchar(255) DEFAULT NULL,
  `driver_tlc_fhv_license_num` varchar(255) DEFAULT NULL,
  `driver_tlc_fhv_license_start` datetime DEFAULT NULL,
  `driver_tlc_fhv_license_expire` datetime DEFAULT NULL,
  `driver_tlc_fhv_license_status` int(11) DEFAULT NULL,
  `background_check` varchar(255) DEFAULT NULL,
  `background_check_start` datetime DEFAULT NULL,
  `background_check_expire` datetime DEFAULT NULL,
  `background_check_status` int(1) DEFAULT NULL,
  `driving_record` varchar(255) DEFAULT NULL,
  `driving_record_start` datetime DEFAULT NULL,
  `driving_record_expire` datetime DEFAULT NULL,
  `driving_record_status` int(11) DEFAULT '0',
  `drug_screen` varchar(255) DEFAULT NULL,
  `drug_screen_start` datetime DEFAULT NULL,
  `drug_screen_expire` datetime DEFAULT NULL,
  `drug_screen_status` int(11) DEFAULT '0',
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zipcode` varchar(255) DEFAULT NULL,
  `vehicle_id` int(11) DEFAULT NULL,
  `allow_pets` int(1) DEFAULT NULL,
  `allow_wheelchair` int(1) DEFAULT NULL,
  `duty_status` int(11) DEFAULT NULL,
  `app_language` varchar(255) DEFAULT NULL,
  `device_token` varchar(255) DEFAULT NULL,
  `device_id` varchar(255) DEFAULT NULL,
  `mobile_os` varchar(255) DEFAULT NULL,
  `object_id` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `approvable_mail` varchar(255) DEFAULT NULL,
  `base_approved` int(1) DEFAULT NULL,
  `status` int(1) NOT NULL,
  `dsp_percent` double(10,2) DEFAULT '0.00',
  `cancel_count` int(11) DEFAULT NULL,
  `permit_id` varchar(255) DEFAULT NULL,
  `authorizenet_id` varchar(255) DEFAULT NULL,
  `accessible_menu` varchar(255) DEFAULT NULL,
  `remember_token` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table employee
# ------------------------------------------------------------

DROP TABLE IF EXISTS `employee`;

CREATE TABLE `employee` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `gender` varchar(255) NOT NULL,
  `social_security_number` varchar(255) DEFAULT NULL,
  `date_of_birth` datetime NOT NULL,
  `physical_exam` varchar(255) DEFAULT NULL,
  `certificate_name` varchar(255) DEFAULT NULL,
  `certificate_start` datetime DEFAULT NULL,
  `certificate_end` datetime DEFAULT NULL,
  `work_start` datetime DEFAULT NULL,
  `work_end` datetime DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `manager` int(11) unsigned DEFAULT NULL,
  `type` varchar(255) NOT NULL COMMENT 'could be part-time, full-time, consultant, volunteer, etc.',
  `status` int(1) DEFAULT '1',
  `background_check` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `agency_id` int(11) unsigned NOT NULL,
  `weekly_hours_worked` varchar(10) DEFAULT '0h0m',
  `weekly_working_time_limitation` int(11) DEFAULT NULL,
  `employee_type` enum('HOURLY','ANNUALLY') DEFAULT NULL,
  `pin` varchar(255) DEFAULT NULL,
  `rules_and_regus_doc_id` int(11) unsigned DEFAULT NULL COMMENT 'That refers to file_upload_id, that rules and regulation doc is for employee and need to be signed by every employee',
  `company_id` int(11) NOT NULL,
  `organization_id` int(11) DEFAULT NULL,
  `accrual_period_start` date DEFAULT NULL,
  `rate` double(11,2) DEFAULT NULL,
  `vacation_start` datetime DEFAULT NULL,
  `vacation_end` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_employee_agency1_idx` (`agency_id`),
  KEY `fk_employee_1_idx` (`organization_id`),
  KEY `fk_employee_company1_idx` (`company_id`),
  KEY `fk_employee_file_upload_1_idx` (`rules_and_regus_doc_id`),
  KEY `fk_employee_manager_fk1` (`manager`),
  CONSTRAINT `fk_employee_file_upload_1` FOREIGN KEY (`rules_and_regus_doc_id`) REFERENCES `file_upload` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_employee_manager_fk1` FOREIGN KEY (`manager`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table employee_clocking
# ------------------------------------------------------------

DROP TABLE IF EXISTS `employee_clocking`;

CREATE TABLE `employee_clocking` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `employee_id` int(11) unsigned NOT NULL,
  `check_in_time` datetime NOT NULL,
  `check_in_signature_id` int(11) unsigned DEFAULT NULL COMMENT 'this part refers the table of signature',
  `check_out_time` datetime DEFAULT NULL,
  `check_out_signature_id` int(11) DEFAULT NULL COMMENT 'this part refers the table of signature',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `week_period` varchar(255) DEFAULT NULL COMMENT 'this will help to identify which week, the content could be 11/06/2017-11/12/2017, this will be easier for the accountant',
  PRIMARY KEY (`id`),
  KEY `fk_employee_clocking_idx` (`employee_id`),
  CONSTRAINT `fk_employee_clocking_employee1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table employee_has_activity
# ------------------------------------------------------------

DROP TABLE IF EXISTS `employee_has_activity`;

CREATE TABLE `employee_has_activity` (
  `employee_id` int(11) unsigned NOT NULL,
  `activity_id` int(11) unsigned NOT NULL,
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `fk_employee_has_activity_activity1_idx` (`activity_id`),
  KEY `fk_employee_has_activity_employee1_idx` (`employee_id`),
  CONSTRAINT `fk_employee_has_activity_activity1` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_employee_has_activity_employee1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table employee_payment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `employee_payment`;

CREATE TABLE `employee_payment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employee_id` int(11) unsigned DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `spread_of_hour` double(16,2) DEFAULT NULL,
  `work_hours` double(16,2) DEFAULT NULL,
  `total_hours` double(16,2) DEFAULT NULL,
  `payment_for_that_day` double(16,2) DEFAULT NULL,
  `adjustment` double(16,2) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `employee_payment_e_id` (`employee_id`),
  CONSTRAINT `employee_payment_e_id` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table file_upload
# ------------------------------------------------------------

DROP TABLE IF EXISTS `file_upload`;

CREATE TABLE `file_upload` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `entity` varchar(255) NOT NULL COMMENT 'a entity name that such document belong',
  `entity_id` int(11) unsigned NOT NULL COMMENT 'an id id row entity that such document belong',
  `file_class` varchar(255) NOT NULL COMMENT 'a unique name for a type document ej MEDICALCERTIFICATION',
  `file_name` varchar(500) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_size` int(11) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `file_url` varchar(255) DEFAULT NULL,
  `status` int(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table food_allergy
# ------------------------------------------------------------

DROP TABLE IF EXISTS `food_allergy`;

CREATE TABLE `food_allergy` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `added_by_user` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table group_member
# ------------------------------------------------------------

DROP TABLE IF EXISTS `group_member`;

CREATE TABLE `group_member` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `user_id` int(11) unsigned NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_group_member_user1_idx` (`user_id`),
  CONSTRAINT `fk_group_member_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



# Dump of table health_insurance_claim
# ------------------------------------------------------------

DROP TABLE IF EXISTS `health_insurance_claim`;

CREATE TABLE `health_insurance_claim` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `insurance_plan_name` varchar(255) NOT NULL COMMENT 'Could be MEDICARE , MEDICAID, TRICARE-CHAMPUS, CHAMPVA, GROUP-HEALTH-PLAN, FECA-BLKLUNG, OTHER',
  `relationship_to_insured` varchar(255) DEFAULT NULL COMMENT 'Could be Self , Spouse, Child, Other',
  `patient_marital_status` varchar(255) DEFAULT NULL COMMENT 'Could be Single, Married, Other',
  `patient_employment_status` varchar(255) DEFAULT NULL COMMENT 'Could be Employed, Full-time Student, Part-time Student',
  `other_insured_first_name` varchar(255) DEFAULT NULL,
  `other_insured_middle_name` varchar(255) DEFAULT NULL,
  `other_insured_last_name` varchar(255) DEFAULT NULL,
  `other_insured_policy_or_group_no` varchar(255) DEFAULT NULL,
  `other_insured_dob` datetime DEFAULT NULL,
  `other_insured_sex` varchar(255) DEFAULT NULL COMMENT 'Could be Male, Female',
  `other_insured_employer_school_name` varchar(255) DEFAULT NULL,
  `other_insured_insurance_insurance_plan_name` varchar(255) DEFAULT NULL COMMENT 'Could be MEDICARE , MEDICAID, TRICARE-CHAMPUS, CHAMPVA, GROUP-HEALTH-PLAN, FECA-BLKLUNG, OTHER',
  `patient_condtion_relation` varchar(255) DEFAULT NULL COMMENT 'Could be EMPLOYMENT , AUTO ACCIDENT, OTHER ACCIDENT',
  `auto_accident_place` varchar(255) DEFAULT NULL COMMENT 'Value will be exist if patient_condition_relation is ''Auto Accident''',
  `reserved_local_use` varchar(255) DEFAULT NULL,
  `patient_signature_id` int(11) unsigned DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` int(11) unsigned DEFAULT '2',
  `insured_id_no` varchar(255) NOT NULL,
  `insured_first_name` varchar(255) NOT NULL,
  `insured_middle_name` varchar(255) DEFAULT NULL,
  `insured_last_name` varchar(255) NOT NULL,
  `insured_address` varchar(255) NOT NULL,
  `insured_city` varchar(255) NOT NULL,
  `insured_state` varchar(255) NOT NULL,
  `insured_zipcode` varchar(255) NOT NULL,
  `insured_phone_no` varchar(255) NOT NULL,
  `insured_policy_group_or_feca_no` varchar(255) DEFAULT NULL,
  `insured_dob` datetime NOT NULL,
  `insured_sex` varchar(255) NOT NULL COMMENT 'Could be Male, Female',
  `insured_employer_school_name` varchar(255) DEFAULT NULL,
  `insured_insurance_plan_name_or_program_name` varchar(255) DEFAULT NULL COMMENT 'Could be MEDICARE , MEDICAID, TRICARE-CHAMPUS, CHAMPVA, GROUP-HEALTH-PLAN, FECA-BLKLUNG, OTHER',
  `insured_another_health_plan` tinyint(1) DEFAULT NULL,
  `insured_signature_id` int(11) unsigned DEFAULT NULL,
  `current_illness_injury_pregnancy_date` date DEFAULT NULL,
  `first_similar_illness_date` date DEFAULT NULL,
  `patient_unable_work_current_occupation_from_date` date DEFAULT NULL,
  `hospitalization_related_to_current_services_date_from` date DEFAULT NULL,
  `hospitalization_related_to_current_services_date_to` date DEFAULT NULL,
  `outside_lab` tinyint(1) DEFAULT NULL,
  `charges` decimal(10,3) DEFAULT NULL,
  `medicaid_resubmission_code` varchar(255) DEFAULT NULL,
  `original_ref_no` varchar(255) DEFAULT NULL,
  `prior_authorization_no` varchar(255) DEFAULT NULL,
  `referring_provider_name` varchar(255) DEFAULT NULL,
  `referring_provider_npi` varchar(255) DEFAULT NULL,
  `reserved_for_local_use` varchar(255) DEFAULT NULL,
  `diagnosis_or_nature_of_illness_or_injury_type_1` varchar(255) DEFAULT NULL,
  `diagnosis_or_nature_of_illness_or_injury_type_2` varchar(255) DEFAULT NULL,
  `diagnosis_or_nature_of_illness_or_injury_type_3` varchar(255) DEFAULT NULL,
  `diagnosis_or_nature_of_illness_or_injury_type_4` varchar(255) DEFAULT NULL,
  `federal_tax_id_no_ssn` varchar(255) DEFAULT NULL,
  `federal_tax_id_no_ein` varchar(255) DEFAULT NULL,
  `patient_account_no` varchar(255) DEFAULT NULL,
  `accept_assignment` tinyint(1) DEFAULT NULL,
  `total_charge` decimal(10,3) DEFAULT NULL,
  `amount_paid` decimal(10,3) DEFAULT NULL,
  `balance_due` decimal(10,3) DEFAULT NULL,
  `physician_or_supplier_signature_id` int(11) unsigned DEFAULT NULL,
  `patient_unable_work_current_occupation_from_to` date DEFAULT NULL,
  `service_facility_location_information_a` varchar(255) DEFAULT NULL,
  `service_facility_location_information_b` varchar(255) DEFAULT NULL,
  `billing_provider_info_and_ph_no_a` varchar(255) DEFAULT NULL,
  `billing_provider_info_and_ph_no_b` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_health_insurance_claim_file_upload_patient_signature` (`patient_signature_id`),
  KEY `fk_health_insurance_claim_file_upload_insured_signature` (`insured_signature_id`),
  KEY `fk_health_insurance_claim_doc_phy_or_sup_sign` (`physician_or_supplier_signature_id`),
  KEY `fk_health_insurance_claim_user1_idx` (`user_id`),
  CONSTRAINT `fk_health_insurance_claim_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table structure for table `health_insurance_claim`';



# Dump of table health_status
# ------------------------------------------------------------

DROP TABLE IF EXISTS `health_status`;

CREATE TABLE `health_status` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `primary_physician` varchar(255) DEFAULT NULL,
  `clinic_hmo` varchar(255) DEFAULT NULL,
  `hospital` varchar(255) DEFAULT NULL,
  `other` varchar(255) DEFAULT NULL,
  `date_of_last_PCP` datetime DEFAULT NULL,
  `participant_illness_and_disability` varchar(255) DEFAULT NULL,
  `have_assistive_device` varchar(255) DEFAULT NULL,
  `understand_others` int(11) DEFAULT NULL,
  `hospitalized_or_emergency_room_last_6_months` varchar(255) DEFAULT NULL,
  `other_comment` varchar(1024) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `reserved_1` varchar(255) DEFAULT NULL,
  `reserved_2` varchar(255) DEFAULT NULL,
  `reserved_3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table housing
# ------------------------------------------------------------

DROP TABLE IF EXISTS `housing`;

CREATE TABLE `housing` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `type_of_Housing` varchar(255) DEFAULT NULL COMMENT 'Checkbox Options: Multi-unit Housing, Single family home, Owns, Rents, Other',
  `other_comment` varchar(1024) DEFAULT NULL,
  `home_safety_checklist` varchar(255) DEFAULT NULL COMMENT 'Checkbox Options: Accumulated Garbage, Carbon Monoxide detectors not present/not working, Doorway widths are iadequate, Loose scatter rugs present in one or more rooms, Lamp or light switch within easy reach of the bed, No rubber mats or non-slip decals in the tub or shower, Smoke detectors not present/not working, Telephone and appliance cords are strung across area where people walk, Traffic lane from the bedroom to the bathroom is not clear of obstacles, No handrails on the stairway, Bad odors, Floors and stairway dirty and cluttered, No light in the bathroom or in the hallway, No locks on doors or not working, No grab bar in tub or shower, Stairways are not in good condition, Other (Specify)',
  `hsc_other_comment` varchar(1024) DEFAULT NULL,
  `is_neighborhodd_safety_an_issue` int(11) DEFAULT NULL COMMENT 'Yes or No',
  `comment` varchar(1024) DEFAULT NULL,
  `other_home_safety` varchar(255) DEFAULT NULL,
  `other_housing` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `reserved_1` varchar(255) DEFAULT NULL,
  `reserved_2` varchar(255) DEFAULT NULL,
  `reserved_3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table iadls
# ------------------------------------------------------------

DROP TABLE IF EXISTS `iadls`;

CREATE TABLE `iadls` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `housing_cleaning_self` int(11) unsigned DEFAULT NULL,
  `housing_cleaning_formal_service` int(11) unsigned DEFAULT NULL,
  `housing_cleaning_informal_support` int(11) unsigned DEFAULT NULL,
  `housing_cleaning_other` int(11) unsigned DEFAULT NULL,
  `housing_cleaning_other_comment` varchar(255) DEFAULT NULL,
  `shopping_self` int(11) unsigned DEFAULT NULL,
  `shopping_formal_service` int(11) unsigned DEFAULT NULL,
  `shopping_informal_support` int(11) unsigned DEFAULT NULL,
  `shopping_other` int(11) unsigned DEFAULT NULL,
  `shopping_other_comment` varchar(255) DEFAULT NULL,
  `laundry_self` int(11) unsigned DEFAULT NULL,
  `laundry_formal_service` int(11) unsigned DEFAULT NULL,
  `laundry_informal_support` int(11) unsigned DEFAULT NULL,
  `laundry_other` int(11) unsigned DEFAULT NULL,
  `laundry_other_comment` varchar(255) DEFAULT NULL,
  `transportation_self` int(11) unsigned DEFAULT NULL,
  `transportation_formal_service` int(11) unsigned DEFAULT NULL,
  `transportation_informal_support` int(11) unsigned DEFAULT NULL,
  `transportation_other` int(11) unsigned DEFAULT NULL,
  `transportation_other_comment` varchar(255) DEFAULT NULL,
  `prepare_self` int(11) unsigned DEFAULT NULL,
  `prepare_formal_service` int(11) unsigned DEFAULT NULL,
  `prepare_informal_support` int(11) unsigned DEFAULT NULL,
  `prepare_other` int(11) unsigned DEFAULT NULL,
  `prepare_other_comment` varchar(255) DEFAULT NULL,
  `business_self` int(11) unsigned DEFAULT NULL,
  `business_formal_service` int(11) unsigned DEFAULT NULL,
  `business_informal_support` int(11) unsigned DEFAULT NULL,
  `business_other` int(11) unsigned DEFAULT NULL,
  `business_other_comment` varchar(255) DEFAULT NULL,
  `use_self` int(11) unsigned DEFAULT NULL,
  `use_formal_service` int(11) unsigned DEFAULT NULL,
  `use_informal_support` int(11) unsigned DEFAULT NULL,
  `use_other` int(11) unsigned DEFAULT NULL,
  `use_other_comment` varchar(255) DEFAULT NULL,
  `medication_self` int(11) unsigned DEFAULT NULL,
  `medication_formal_service` int(11) unsigned DEFAULT NULL,
  `medication_informal_support` int(11) unsigned DEFAULT NULL,
  `medication_other` int(11) unsigned DEFAULT NULL,
  `medication_other_comment` varchar(255) DEFAULT NULL,
  `bathing_no_supervision` int(11) unsigned DEFAULT NULL,
  `bathing_intermittent_supervision` int(11) unsigned DEFAULT NULL,
  `bathing_physical_assistance` int(11) unsigned DEFAULT NULL,
  `bathing_not_participater` int(11) unsigned DEFAULT NULL,
  `hygiene_no_supervision` int(11) unsigned DEFAULT NULL,
  `hygiene_intermittent_supervision` int(11) unsigned DEFAULT NULL,
  `hygiene_physical_assistance` int(11) unsigned DEFAULT NULL,
  `hygiene_not_participater` int(11) unsigned DEFAULT NULL,
  `dressing_no_supervision` int(11) unsigned DEFAULT NULL,
  `dressing_intermittent_supervision` int(11) unsigned DEFAULT NULL,
  `dressing_physical_assistance` int(11) unsigned DEFAULT NULL,
  `dressing_not_participater` int(11) unsigned DEFAULT NULL,
  `mobility_no_supervision` int(11) unsigned DEFAULT NULL,
  `mobility_intermittent_supervision` int(11) unsigned DEFAULT NULL,
  `mobility_physical_assistance` int(11) unsigned DEFAULT NULL,
  `mobility_not_participater` int(11) unsigned DEFAULT NULL,
  `transfer_no_supervision` int(11) unsigned DEFAULT NULL,
  `transfer_intermittent_supervision` int(11) unsigned DEFAULT NULL,
  `transfer_physical_assistance` int(11) unsigned DEFAULT NULL,
  `transfer_not_participater` int(11) unsigned DEFAULT NULL,
  `toileting_no_supervision` int(11) unsigned DEFAULT NULL,
  `toileting_intermittent_supervision` int(11) unsigned DEFAULT NULL,
  `toileting_physical_assistance` int(11) unsigned DEFAULT NULL,
  `toileting_not_participater` int(11) unsigned DEFAULT NULL,
  `eating_no_supervision` int(11) unsigned DEFAULT NULL,
  `eating_intermittent_supervision` int(11) unsigned DEFAULT NULL,
  `eating_physical_assistance` int(11) unsigned DEFAULT NULL,
  `eating_not_participater` int(11) unsigned DEFAULT NULL,
  `activity_status_housing_cleaning` varchar(255) DEFAULT NULL COMMENT 'Options: Self, Informal Support, Formal Service',
  `activity_status_housing_cleaning_other` varchar(255) DEFAULT NULL,
  `activity_status_shopping` varchar(255) DEFAULT NULL COMMENT 'Options: Self, Informal Support, Formal Service',
  `activity_status_shopping_other` varchar(255) DEFAULT NULL,
  `activity_status_laundry` varchar(255) DEFAULT NULL COMMENT 'Options: Self, Informal Support, Formal Service',
  `activity_status_laundry_other` varchar(255) DEFAULT NULL,
  `activity_status_use_transportation` varchar(255) DEFAULT NULL COMMENT 'Options: Self, Informal Support, Formal Service',
  `activity_status_use_transportation_other` varchar(255) DEFAULT NULL,
  `activity_status_prepare_cook_meal` varchar(255) DEFAULT NULL COMMENT 'Options: Self, Informal Support, Formal Service',
  `activity_status_prepare_cook_meal_other` varchar(255) DEFAULT NULL,
  `activity_status_handle_personal_finance_business` varchar(255) DEFAULT NULL COMMENT 'Options: Self, Informal Support, Formal Service',
  `activity_status_handle_personal_finance_business_other` varchar(255) DEFAULT NULL,
  `activity_status_use_template` varchar(255) DEFAULT NULL COMMENT 'Options: Self, Informal Support, Formal Service',
  `activity_status_use_template_other` varchar(255) DEFAULT NULL,
  `activity_status_administering_of_medication` varchar(255) DEFAULT NULL COMMENT 'Options: Self, Informal Support, Formal Service',
  `activity_status_administering_of_medication_other` varchar(255) DEFAULT NULL,
  `bathing` varchar(255) DEFAULT NULL,
  `personal_hygiene` varchar(255) DEFAULT NULL,
  `dressing` varchar(255) DEFAULT NULL,
  `mobility` varchar(255) DEFAULT NULL,
  `transfer` varchar(255) DEFAULT NULL,
  `toileting` varchar(255) DEFAULT NULL,
  `eating` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `reserved_1` varchar(255) DEFAULT NULL,
  `reserved_2` varchar(255) DEFAULT NULL,
  `reserved_3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table information_support
# ------------------------------------------------------------

DROP TABLE IF EXISTS `information_support`;

CREATE TABLE `information_support` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `have_help_with_care` int(11) unsigned DEFAULT NULL,
  `support_people_first_name` varchar(255) DEFAULT NULL,
  `support_people_middle_name` varchar(255) DEFAULT NULL,
  `support_people_last_name` varchar(255) DEFAULT NULL,
  `support_people_relation` varchar(255) DEFAULT NULL,
  `support_people_email` varchar(255) DEFAULT NULL,
  `support_people` varchar(255) DEFAULT NULL,
  `support_people_home_phone` varchar(255) DEFAULT NULL,
  `support_people_work_phone` varchar(255) DEFAULT NULL,
  `support_people_cell_phone` varchar(255) DEFAULT NULL,
  `involvement` varchar(255) DEFAULT NULL,
  `customer_has_good_relationship_with_support` int(11) unsigned DEFAULT NULL,
  `customer_will_accept_help_to_remain_home_or_independence` int(11) unsigned DEFAULT NULL,
  `factors_limit_informal_support_involvement` varchar(255) DEFAULT NULL,
  `sercice_as_respite_for_caregiver` varchar(255) DEFAULT NULL,
  `can_other_informal_support_help_to_relieve_caregiver` int(11) unsigned DEFAULT NULL,
  `can_other_informal_support_help_to_relieve_caregiver_comment` varchar(255) DEFAULT NULL,
  `has_affiliation_to_provide_assistance` int(11) unsigned DEFAULT NULL,
  `has_affiliation_to_provide_assistance_comment` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `reserved_1` varchar(255) DEFAULT NULL,
  `reserved_2` varchar(255) DEFAULT NULL,
  `reserved_3` varchar(255) DEFAULT NULL,
  `support_people_address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ingredient
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ingredient`;

CREATE TABLE `ingredient` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ingredient_has_allergy
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ingredient_has_allergy`;

CREATE TABLE `ingredient_has_allergy` (
  `food_allergy_id` int(11) unsigned NOT NULL,
  `ingredient_id` int(11) NOT NULL,
  PRIMARY KEY (`food_allergy_id`,`ingredient_id`),
  KEY `fk_ingredient_has_allergy_ingredient` (`ingredient_id`),
  CONSTRAINT `fk_ingredient_has_allergy_allergy` FOREIGN KEY (`food_allergy_id`) REFERENCES `food_allergy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ingredient_has_allergy_ingredient` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredient` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table insurance_history
# ------------------------------------------------------------

DROP TABLE IF EXISTS `insurance_history`;

CREATE TABLE `insurance_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `insurance_id` int(11) unsigned NOT NULL,
  `authorization_start` datetime DEFAULT NULL,
  `authorization_end` datetime DEFAULT NULL,
  `insurance_code` varchar(255) DEFAULT NULL,
  `authorization_code` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `reserved_1` varchar(255) DEFAULT NULL,
  `reserved_2` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table leave
# ------------------------------------------------------------

DROP TABLE IF EXISTS `leave`;

CREATE TABLE `leave` (
  `employee_id` int(11) unsigned DEFAULT NULL,
  `sick_days_limitation` int(4) NOT NULL DEFAULT '10' COMMENT 'Limit of sick days',
  `sick_days_used` int(4) NOT NULL DEFAULT '10' COMMENT 'Sick days used',
  `ask_for_leave_days_limitation` int(4) NOT NULL DEFAULT '10' COMMENT 'leave day limit',
  `ask_for_leave_days_used` int(4) NOT NULL DEFAULT '0' COMMENT 'leave days used by emp',
  `vacation_days_limitation` int(4) NOT NULL DEFAULT '10' COMMENT 'vacation day limit',
  `vacation_days_used` int(4) NOT NULL DEFAULT '0' COMMENT 'vacations days used of emp',
  `year` int(4) NOT NULL COMMENT 'year of these values',
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned DEFAULT NULL,
  `sick_days` int(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `emp_id` (`employee_id`,`year`),
  KEY `emp_leave_fk_employee` (`employee_id`),
  CONSTRAINT `emp_leave_fk_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table meal
# ------------------------------------------------------------

DROP TABLE IF EXISTS `meal`;

CREATE TABLE `meal` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `restaurant_id` int(11) unsigned NOT NULL,
  `price` decimal(10,3) DEFAULT NULL,
  `meal_class` varchar(255) NOT NULL COMMENT 'could be appetizers/entree/desert',
  `name` varchar(255) NOT NULL,
  `ingredients` varchar(1000) DEFAULT NULL,
  `notes` varchar(1000) DEFAULT NULL,
  `verified_by_nutritionist` int(11) DEFAULT '0',
  `meal_image_id` int(11) unsigned DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `cuisine` varchar(255) DEFAULT NULL COMMENT 'could be breakfast/lunch/dinner',
  `meal_type` varchar(255) DEFAULT NULL,
  `meal_status` int(11) DEFAULT '0',
  `agency_id` int(11) unsigned DEFAULT NULL,
  `selected` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_meal_restaurant` (`restaurant_id`),
  CONSTRAINT `fk_meal_restaurant` FOREIGN KEY (`restaurant_id`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table meal_has_ingredient
# ------------------------------------------------------------

DROP TABLE IF EXISTS `meal_has_ingredient`;

CREATE TABLE `meal_has_ingredient` (
  `meal_id` int(11) unsigned NOT NULL,
  `ingredient_id` int(11) NOT NULL,
  PRIMARY KEY (`meal_id`,`ingredient_id`),
  KEY `fk_meal_has_ingredient_ingredient` (`ingredient_id`),
  CONSTRAINT `fk_meal_has_ingredient_ingredient` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredient` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_meal_has_ingredient_meal` FOREIGN KEY (`meal_id`) REFERENCES `meal` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table media
# ------------------------------------------------------------

DROP TABLE IF EXISTS `media`;

CREATE TABLE `media` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(25) DEFAULT NULL,
  `category` varchar(10) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `path` varchar(100) DEFAULT NULL,
  `agency_id` int(11) unsigned DEFAULT NULL,
  `company_id` int(11) unsigned DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `media_agency_id` (`agency_id`),
  KEY `media_company_id` (`company_id`),
  CONSTRAINT `media_agency_id` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `media_company_id` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table medication
# ------------------------------------------------------------

DROP TABLE IF EXISTS `medication`;

CREATE TABLE `medication` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `insurance_id` int(11) unsigned DEFAULT NULL COMMENT 'refer the organization_id of organization table',
  `insurance_eligiable` varchar(255) DEFAULT NULL,
  `medical_condition` varchar(1000) DEFAULT NULL,
  `ambulatory_status` enum('self','cane','walker','crutches','wheelchair','other') DEFAULT NULL,
  `diagonosis` varchar(255) DEFAULT NULL,
  `home_attendant_agency_id` int(11) unsigned DEFAULT NULL COMMENT 'refer the table of organization',
  `visiting_nurse` varchar(255) DEFAULT NULL,
  `visiting_nurse_schedule` varchar(255) DEFAULT NULL,
  `allergies` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `service_plan_id` int(11) unsigned DEFAULT NULL COMMENT 'refer the table of service_plan',
  `family_doctor_id` int(11) unsigned DEFAULT NULL COMMENT 'refer the table of organization',
  `expert_doctor_id` int(11) unsigned DEFAULT NULL COMMENT 'refer the table of organization',
  `pharmacy_id` int(11) unsigned DEFAULT NULL COMMENT 'refer the table of organization',
  `medicine_id` int(11) unsigned DEFAULT NULL COMMENT 'refer the table of medicine',
  `mltc_id` int(11) unsigned DEFAULT NULL COMMENT 'refer the table of organization',
  PRIMARY KEY (`id`),
  KEY `fk_medication_1_idx` (`insurance_id`),
  KEY `fk_medication_2_idx` (`home_attendant_agency_id`),
  KEY `fk_medication_4_idx` (`family_doctor_id`),
  KEY `fk_medication_5_idx` (`expert_doctor_id`),
  KEY `fk_medication_5_idx1` (`pharmacy_id`),
  KEY `fk_medication_7_idx` (`medicine_id`),
  KEY `fk_medication_3` (`service_plan_id`),
  CONSTRAINT `fk_medication_1` FOREIGN KEY (`insurance_id`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_medication_2` FOREIGN KEY (`home_attendant_agency_id`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_medication_3` FOREIGN KEY (`service_plan_id`) REFERENCES `service_plan` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_medication_4` FOREIGN KEY (`family_doctor_id`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_medication_5` FOREIGN KEY (`expert_doctor_id`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_medication_6` FOREIGN KEY (`pharmacy_id`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_medication_7` FOREIGN KEY (`medicine_id`) REFERENCES `medicine` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table medicine
# ------------------------------------------------------------

DROP TABLE IF EXISTS `medicine`;

CREATE TABLE `medicine` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `medicine_name` varchar(255) DEFAULT NULL,
  `medicine_use_ways` varchar(255) DEFAULT NULL,
  `medicine_type` varchar(255) DEFAULT NULL,
  `medicine_prescribed_by_id` varchar(255) DEFAULT NULL COMMENT 'refer the doctor_office_id in the table of organization ',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table medicine_taken
# ------------------------------------------------------------

DROP TABLE IF EXISTS `medicine_taken`;

CREATE TABLE `medicine_taken` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `medication_name_one` varchar(255) DEFAULT NULL,
  `medication_dose_one` varchar(255) DEFAULT NULL,
  `reason_taken_one` varchar(255) DEFAULT NULL,
  `medication_name_two` varchar(255) DEFAULT NULL,
  `medication_dose_two` varchar(255) DEFAULT NULL,
  `reason_taken_two` varchar(255) DEFAULT NULL,
  `medication_name_three` varchar(255) DEFAULT NULL,
  `medication_dose_three` varchar(255) DEFAULT NULL,
  `reason_taken_three` varchar(255) DEFAULT NULL,
  `medication_name_four` varchar(255) DEFAULT NULL,
  `medication_dose_four` varchar(255) DEFAULT NULL,
  `reason_taken_four` varchar(255) DEFAULT NULL,
  `medication_name_five` varchar(255) DEFAULT NULL,
  `medication_dose_five` varchar(255) DEFAULT NULL,
  `reason_taken_five` varchar(255) DEFAULT NULL,
  `medication_name_six` varchar(255) DEFAULT NULL,
  `medication_dose_six` varchar(255) DEFAULT NULL,
  `reason_taken_six` varchar(255) DEFAULT NULL,
  `medication_name_seven` varchar(255) DEFAULT NULL,
  `medication_dose_seven` varchar(255) DEFAULT NULL,
  `reason_taken_seven` varchar(255) DEFAULT NULL,
  `medication_name_eight` varchar(255) DEFAULT NULL,
  `medication_dose_eight` varchar(255) DEFAULT NULL,
  `reason_taken_eight` varchar(255) DEFAULT NULL,
  `adverser_reaction_allergy_sensitivity` int(11) unsigned DEFAULT NULL,
  `adverser_reaction_allergy_sensitivity_comment` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `reserved_1` varchar(255) DEFAULT NULL,
  `reserved_2` varchar(255) DEFAULT NULL,
  `reserved_3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table menu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `angular_url` varchar(255) NOT NULL,
  `page` varchar(255) NOT NULL,
  `class` varchar(255) NOT NULL,
  `img_url` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `display_order` int(11) DEFAULT '0',
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;

INSERT INTO `menu` (`id`, `name`, `url`, `angular_url`, `page`, `class`, `img_url`, `created_at`, `display_order`, `status`)
VALUES
	(1,'Dashboard','Dashboard','Dashboard','1','0','Dashboard','2017-08-27 23:06:11',1,1),
	(2,'Company','Company','Company','2','0','Company','2017-08-27 23:06:13',2,1),
	(3,'Platform Admin','PlatformAdmin','PlatformAdmin','3','0','PlatformAdmin','2017-08-27 23:06:17',3,1),
	(4,'Senior Center Admin','SeniorCenterAdmin','SeniorCenterAdmin','4','0','SeniorCenterAdmin','2017-08-27 23:06:20',4,1),
	(5,'Company Admin','CompanyAdmin','CompanyAdmin','5','0','CompanyAdmin','2017-08-27 23:06:24',5,1),
	(6,'Seniors','Seniors','Seniors','6','0','User','2017-09-04 06:20:02',6,1),
	(7,'Visit','Visit','Visit','7','0','Visit','2017-09-04 06:20:02',7,1),
	(8,'Employee','Employee','Employee','8','0','Employee','2017-09-04 06:20:02',8,1),
	(9,'Training','Training','Training','9','0','Training','2017-09-04 06:20:02',9,1),
	(10,'Billing','Billing','Billing','10','0','Billing','2017-09-04 06:20:02',10,1),
	(11,'Content','Content','Content','11','0','Content','2017-09-04 06:20:02',11,1),
	(12,'Driver','Driver','Driver','12','0','Driver','2017-09-04 06:20:02',12,1),
	(13,'Meal','Meal','Meal','13','0','Meal','2017-09-04 06:20:02',13,1),
	(16,'Role','Role','Role','14','0','Role','2017-09-04 06:20:02',14,1),
	(17,'Senior Center','SeniorCenter','SeniorCenter','15','0','SeniorCenter','2018-01-08 05:24:07',15,1),
	(18,'Table','Table','Table','16','0','Table','2018-01-08 05:24:08',16,1),
	(19,'Seat','Seat','Seat','17','0','Seat','2018-01-08 05:24:09',17,1),
	(20,'Seating Map','Seating Map','Seating Map','18','0','Seating Map','2018-01-08 05:24:11',18,1),
	(21,'Trip','Trip','Trip','19','0','Trip','2018-01-08 05:24:12',19,1),
	(23,'Organization','Organization','Organization','20','0','Organization','2018-01-08 05:24:14',20,1),
	(24,'Activity','Activity','Activity','21','0','Activity','2018-01-14 23:43:34',21,1),
	(25,'Report','Report','Report','22','0','Report','2018-01-14 23:43:36',22,1),
	(26,'Senior Stats','SeniorStats','SeniorStats','23','0','SeniorStats','2018-01-14 23:43:36',23,1),
	(27,'Check In/Out','CheckInOut','CheckInOut','24','0','CheckInOut','2018-01-15 07:55:35',24,1),
	(28,'Vehicle','Vehicle','Vehicle','25','0','Vehicle','2018-01-15 07:55:35',25,1),
	(29,'Media','Media','Media','26','0','Media','2018-02-08 22:28:08',26,1),
	(30,'Setting','Setting','Setting','27','0','Setting','2018-02-08 22:33:22',27,1),
	(31,'CCTV','cctv','cctv','28','0','cctv','2018-02-08 22:34:28',28,1),
	(32,'Resource','Resource','Resource','29','0','Resource','2018-02-08 22:35:49',29,1),
	(33,'Employee Payment','EmployeePayment','EmployeePayment','30','0','EmployeePayment','2018-02-08 22:37:04',30,1);

/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table message_box
# ------------------------------------------------------------

DROP TABLE IF EXISTS `message_box`;

CREATE TABLE `message_box` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_user` int(11) NOT NULL,
  `to_user` int(11) NOT NULL DEFAULT '-1',
  `group_id` int(11) DEFAULT NULL,
  `message` text NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='store the chat message';



# Dump of table monthly_income
# ------------------------------------------------------------

DROP TABLE IF EXISTS `monthly_income`;

CREATE TABLE `monthly_income` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `individual_social_security_net` varchar(255) DEFAULT NULL,
  `individual_supplemental_security_income` varchar(255) DEFAULT NULL,
  `individual_personal_retirement_income` varchar(255) DEFAULT NULL,
  `individual_interest` varchar(255) DEFAULT NULL,
  `individual_dividends` varchar(255) DEFAULT NULL,
  `individual_salary_wage` varchar(255) DEFAULT NULL,
  `individual_other` varchar(255) DEFAULT NULL,
  `individual_total` varchar(255) DEFAULT NULL,
  `spouse_social_security_net` varchar(255) DEFAULT NULL,
  `spouse_supplemental_security_income` varchar(255) DEFAULT NULL,
  `spouse_personal_retirement_income` varchar(255) DEFAULT NULL,
  `spouse_interest` varchar(255) DEFAULT NULL,
  `spouse_dividends` varchar(255) DEFAULT NULL,
  `spouse_salary_wage` varchar(255) DEFAULT NULL,
  `spouse_other` varchar(255) DEFAULT NULL,
  `spouse_total` varchar(255) DEFAULT NULL,
  `other_family_household_social_security_net` varchar(255) DEFAULT NULL,
  `other_family_household_supplemental_security_income` varchar(255) DEFAULT NULL,
  `other_family_household_personal_retirement_income` varchar(255) DEFAULT NULL,
  `other_family_household_interest` varchar(255) DEFAULT NULL,
  `other_family_household_dividends` varchar(255) DEFAULT NULL,
  `other_family_household_salary_wage` varchar(255) DEFAULT NULL,
  `other_family_household_other` varchar(255) DEFAULT NULL,
  `other_family_household_total` varchar(255) DEFAULT NULL,
  `total_family_household_social_security_net` varchar(255) DEFAULT NULL,
  `total_family_household_supplemental_security_income` varchar(255) DEFAULT NULL,
  `total_family_household_personal_retirement_income` varchar(255) DEFAULT NULL,
  `total_family_household_interest` varchar(255) DEFAULT NULL,
  `total_family_household_dividends` varchar(255) DEFAULT NULL,
  `total_family_household_salary_wage` varchar(255) DEFAULT NULL,
  `total_family_household_other` varchar(255) DEFAULT NULL,
  `total_family_household_total` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `reserved_1` varchar(255) DEFAULT NULL,
  `reserved_2` varchar(255) DEFAULT NULL,
  `reserved_3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table nutrition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `nutrition`;

CREATE TABLE `nutrition` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `height` varchar(255) DEFAULT NULL,
  `weight` varchar(255) DEFAULT NULL,
  `allergy` varchar(255) DEFAULT NULL,
  `body_mass_index` varchar(255) DEFAULT NULL,
  `refrigerator_or_freezer_facilities_adequate` int(11) DEFAULT NULL,
  `open_containers_or_cartons_cut_food` int(11) DEFAULT NULL,
  `physician_prescribed_therapeutic_diet` varchar(255) DEFAULT NULL,
  `alcohol_screening_test` varchar(255) DEFAULT NULL,
  `felt_cut_down_drinking` varchar(255) DEFAULT NULL,
  `annoyed_you_by_criticize_your_drinking` varchar(255) DEFAULT NULL,
  `felt_bad_or_guilty_about_drinking` varchar(255) DEFAULT NULL,
  `need_drink_in_morning` varchar(255) DEFAULT NULL,
  `nutritional_risk_status` varchar(255) DEFAULT NULL,
  `nutritional_score_one` int(11) DEFAULT NULL,
  `nutritional_score_two` int(11) DEFAULT NULL,
  `nutritional_score_three` int(11) DEFAULT NULL,
  `nutritional_score_four` int(11) DEFAULT NULL,
  `nutritional_score_five` int(11) DEFAULT NULL,
  `nutritional_score_six` int(11) DEFAULT NULL,
  `nutritional_score_seven` int(11) DEFAULT NULL,
  `nutritional_score_eight` int(11) DEFAULT NULL,
  `nutritional_score_nine` int(11) DEFAULT NULL,
  `nutritional_score_ten` int(11) DEFAULT NULL,
  `nsi_score` varchar(255) DEFAULT NULL,
  `conclusion` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `reserved_1` varchar(255) DEFAULT NULL,
  `reserved_2` varchar(255) DEFAULT NULL,
  `reserved_3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table orders
# ------------------------------------------------------------

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `meal_id` int(11) unsigned NOT NULL,
  `organzation_id` int(11) unsigned NOT NULL,
  `order_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `delivery_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` int(11) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_order_1` (`user_id`),
  KEY `fk_order_2` (`meal_id`),
  KEY `fk_order_3` (`organzation_id`),
  CONSTRAINT `fk_order_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_2` FOREIGN KEY (`meal_id`) REFERENCES `meal` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_3` FOREIGN KEY (`organzation_id`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table organization
# ------------------------------------------------------------

DROP TABLE IF EXISTS `organization`;

CREATE TABLE `organization` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `license_no` varchar(255) DEFAULT NULL,
  `federal_tax` varchar(255) DEFAULT NULL,
  `federal_tax_start` datetime DEFAULT NULL,
  `federal_tax_expire` datetime DEFAULT NULL,
  `federal_tax_status` int(11) DEFAULT NULL,
  `state_tax` varchar(255) DEFAULT NULL,
  `state_tax_start` datetime DEFAULT NULL,
  `state_tax_expire` datetime DEFAULT NULL,
  `state_tax_status` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL COMMENT 'could be restaurant/doctor office/pharmacy/insurance/broker',
  `code` varchar(255) NOT NULL COMMENT 'could be MTM/NMT',
  `main_contact` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `fax` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `address_one` varchar(255) DEFAULT NULL,
  `address_two` varchar(255) DEFAULT NULL,
  `worktime_start` time NOT NULL DEFAULT '00:00:00',
  `worktime_end` time NOT NULL DEFAULT '23:59:59',
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zipcode` varchar(255) DEFAULT NULL,
  `web_url` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `days_work` varchar(255) DEFAULT NULL COMMENT 'Mon, Tue, Wed, Thu, Fri, Sat, Sun.',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `agency_id` int(11) unsigned DEFAULT NULL,
  `checkbox_list` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table password_reset
# ------------------------------------------------------------

DROP TABLE IF EXISTS `password_reset`;

CREATE TABLE `password_reset` (
  `id` int(10) unsigned NOT NULL,
  `admin_id` int(10) unsigned DEFAULT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  `token` varchar(45) DEFAULT NULL,
  `expired_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_password_reset_admin_1_idx` (`admin_id`),
  KEY `fk_password_reset_user_1_idx` (`user_id`),
  CONSTRAINT `fk_password_reset_admin_1` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `fk_password_reset_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table patient_diagnosis
# ------------------------------------------------------------

DROP TABLE IF EXISTS `patient_diagnosis`;

CREATE TABLE `patient_diagnosis` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `patient_date_of_service_from` datetime NOT NULL,
  `patient_date_of_service_to` datetime NOT NULL,
  `patient_place_of_service` varchar(255) NOT NULL,
  `patient_supplier_emg` varchar(255) NOT NULL,
  `patient_procedures_services_supplies_cpt_hcpcs` varchar(255) NOT NULL,
  `patient_procedures_services_supplies_cpt_modifier` varchar(255) NOT NULL,
  `patient_diagnosis_pointer` varchar(255) NOT NULL COMMENT 'could be value of one of the following columns : diagnosis_or_nature_of_illness_or_injury_type_1, diagnosis_or_nature_of_illness_or_injury_type_2, diagnosis_or_nature_of_illness_or_injury_type_3, diagnosis_or_nature_of_illness_or_injury_type_4',
  `patient_charges` decimal(10,3) NOT NULL,
  `days_or_units` varchar(255) NOT NULL,
  `epsdt_family_plan` varchar(255) NOT NULL,
  `id_qual` varchar(255) NOT NULL,
  `rendering_provider_id_no` varchar(255) NOT NULL,
  `health_insurance_claim_id` int(11) unsigned DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` varchar(45) DEFAULT 'CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP',
  PRIMARY KEY (`id`),
  KEY `fk_patient_diagnosis_health_insurance_claim_id_fk_idx` (`health_insurance_claim_id`),
  CONSTRAINT `fk_patient_diagnosis_health_insurance_claim_id_fk` FOREIGN KEY (`health_insurance_claim_id`) REFERENCES `health_insurance_claim` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table structure for table `patient_diagnosis`';



# Dump of table psycho_social_status
# ------------------------------------------------------------

DROP TABLE IF EXISTS `psycho_social_status`;

CREATE TABLE `psycho_social_status` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `psycho_social_condition` varchar(255) DEFAULT NULL COMMENT 'Options are XXXX',
  `other_comment` varchar(255) DEFAULT NULL,
  `evidence_substance_abuse` int(11) DEFAULT NULL,
  `evidence_substance_abuse_comment` varchar(255) DEFAULT NULL,
  `behavior_reported` int(11) DEFAULT NULL,
  `behavior_reported_comment` varchar(255) DEFAULT NULL,
  `mental_health_problem` int(11) DEFAULT NULL,
  `mental_health_problem_comment` varchar(255) DEFAULT NULL,
  `mental_health_treatment` int(11) DEFAULT NULL,
  `mental_health_treatment_comment` varchar(255) DEFAULT NULL,
  `mental_health_evaluation` int(11) DEFAULT NULL,
  `mental_health_evaluation_comment` varchar(255) DEFAULT NULL,
  `participant_in_program` int(11) DEFAULT NULL,
  `participant_in_program_comment` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `reserved_1` varchar(255) DEFAULT NULL,
  `reserved_2` varchar(255) DEFAULT NULL,
  `reserved_3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table report
# ------------------------------------------------------------

DROP TABLE IF EXISTS `report`;

CREATE TABLE `report` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `base_id` int(11) unsigned NOT NULL COMMENT 'base means the agency, just if we change this one then it will affect lots of the APIs',
  `company_id` int(11) unsigned NOT NULL,
  `admin_id` int(11) unsigned NOT NULL,
  `report_title` varchar(255) NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `data_columns` varchar(255) NOT NULL,
  `format` varchar(255) DEFAULT NULL,
  `download_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `role_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_report_role1_idx` (`role_id`),
  KEY `fk_report_1_idx` (`admin_id`),
  KEY `fk_report_2_idx` (`company_id`),
  CONSTRAINT `fk_report_1` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_report_2` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table report_tracking
# ------------------------------------------------------------

DROP TABLE IF EXISTS `report_tracking`;

CREATE TABLE `report_tracking` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `company_id` int(11) unsigned NOT NULL,
  `agency_id` int(11) unsigned NOT NULL,
  `admin_id` int(11) unsigned NOT NULL,
  `report_name` varchar(255) NOT NULL,
  `report_type` varchar(255) NOT NULL,
  `report_format` varchar(255) DEFAULT NULL,
  `report_title` varchar(255) DEFAULT NULL,
  `report_file_id` varchar(1024) DEFAULT NULL COMMENT 'refer the file_upload_id in the file_upload table',
  `data_columns` varchar(255) NOT NULL,
  `generation_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `note` varchar(255) DEFAULT NULL,
  `status` int(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_report_tracking_1_idx` (`company_id`),
  KEY `fk_report_tracking_1_idx1` (`agency_id`),
  KEY `fk_report_tracking_1_idx2` (`admin_id`),
  CONSTRAINT `fk_report_tracking_1` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_report_tracking_2` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_report_tracking_3` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table reservation
# ------------------------------------------------------------

DROP TABLE IF EXISTS `reservation`;

CREATE TABLE `reservation` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `agency_id` int(11) unsigned DEFAULT NULL,
  `user_id` int(11) unsigned DEFAULT NULL,
  `booked_date` datetime DEFAULT NULL,
  `meal_id` int(11) unsigned DEFAULT NULL,
  `activity_id` int(11) unsigned DEFAULT NULL,
  `table_id` int(11) unsigned DEFAULT NULL,
  `seat_id` int(11) unsigned DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table resource
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resource`;

CREATE TABLE `resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `website` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address_one` varchar(255) DEFAULT NULL,
  `address_two` varchar(255) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `zipcode` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int(11) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `picture_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table review
# ------------------------------------------------------------

DROP TABLE IF EXISTS `review`;

CREATE TABLE `review` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `health_condition` text,
  `card_pulm_condition` varchar(1000) DEFAULT NULL,
  `activity_details` varchar(1000) DEFAULT NULL,
  `pain_details` varchar(1000) DEFAULT NULL,
  `fuctional_status_adls_iadls` varchar(1000) DEFAULT NULL,
  `nutrition_condition` varchar(1000) DEFAULT NULL,
  `communication_hearing_condition` varchar(1000) DEFAULT NULL,
  `psychological_social_condition` varchar(1000) DEFAULT NULL,
  `assessment_reason` varchar(255) DEFAULT NULL,
  `assessment_reason_other` varchar(1500) DEFAULT NULL,
  `assessment_source_information` varchar(255) DEFAULT NULL,
  `assessment_date` datetime DEFAULT NULL,
  `discharge_date` datetime DEFAULT NULL,
  `discharge_reason` varchar(1500) DEFAULT NULL,
  `discharge_comments` varchar(1500) DEFAULT NULL,
  `bp` varchar(255) DEFAULT NULL,
  `pulse` varchar(255) DEFAULT NULL,
  `respiration` varchar(255) DEFAULT NULL,
  `source_of_information` varchar(255) DEFAULT NULL,
  `proxy` varchar(255) DEFAULT NULL,
  `diagnosis` varchar(255) DEFAULT NULL,
  `dnr` varchar(255) DEFAULT NULL,
  `marital_status` varchar(255) DEFAULT NULL COMMENT 'Options: single, married, widowed, divorced, separated, other',
  `race_ethnicity` varchar(255) DEFAULT NULL COMMENT 'Options: Asian, Black/Non-Hispanic, white, hispanic, other',
  `creed` varchar(255) DEFAULT NULL,
  `habbits` varchar(255) DEFAULT NULL,
  `skills` varchar(255) DEFAULT NULL,
  `primary_speaking_language` varchar(255) DEFAULT NULL,
  `primary_writing_language` varchar(255) DEFAULT NULL,
  `primary_understood_language` varchar(255) DEFAULT NULL,
  `living_arrangement` varchar(255) DEFAULT NULL COMMENT 'Options: alone, with spouse only, with spouse and others, with relatives[except spouse], with non-relatives, with domestic partner, other not listed',
  `possible_abuse` varchar(255) DEFAULT NULL COMMENT 'Options: Physical Abuse, Active and passive Neglect, Sexual Abuse, Financial Exploitation, Emotional Abuse, Domestic Violence, Self Neglect, other <eg. Abandonment>',
  `was_abuse_refer_to` varchar(255) DEFAULT NULL COMMENT 'Options: Adult Protective, AAA, Police Agency, Other',
  `is_client_frail` varchar(255) DEFAULT NULL,
  `is_client_disabled` varchar(255) DEFAULT NULL,
  `prompt_information` varchar(255) DEFAULT NULL,
  `participant_signature_id` int(11) unsigned DEFAULT NULL COMMENT 'Refer to the signature_id of the signature table',
  `participant_sign_date` datetime DEFAULT NULL,
  `nurse_name` varchar(255) DEFAULT NULL,
  `nurse_signature_id` int(11) unsigned DEFAULT NULL COMMENT 'Refer to the signature_id of the signature table',
  `nurse_sign_date` datetime DEFAULT NULL,
  `translater_name` varchar(255) DEFAULT NULL,
  `translater_signature_id` int(11) unsigned DEFAULT NULL COMMENT 'Refer to the signature_id of the signature table',
  `signature_date` varchar(255) DEFAULT NULL,
  `home_phone_number` varchar(255) DEFAULT NULL,
  `cellphone_number` varchar(255) DEFAULT NULL,
  `notification_1` varchar(255) DEFAULT NULL COMMENT '*persons receiving SSL is categorically eligiable for Medicaid and should have the Medicaid Card.',
  `notification_2` varchar(255) DEFAULT NULL COMMENT 'Problems to be referred to: Hospital, Nursing Home, Adult Home, Health Assessment, Long Term Care Home Health Care Program, Personal Care Program, Mental Health Assessment, Housing Assessment, Certified Home Health Agency, Licensed Home Care Service Agency, Protective Service for Adult, Other.',
  `comment` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `housing_id` int(11) unsigned DEFAULT NULL,
  `assessment_user_id` int(11) DEFAULT NULL,
  `reserved_2` varchar(255) DEFAULT NULL,
  `reserved_3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_review_1` (`user_id`),
  CONSTRAINT `fk_review_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ride
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ride`;

CREATE TABLE `ride` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `pickup` varchar(255) DEFAULT NULL,
  `dropoff` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `need_trip` int(11) unsigned DEFAULT NULL,
  `user_id` int(11) unsigned DEFAULT NULL,
  `status` int(11) unsigned DEFAULT NULL,
  `ride_line_id` int(11) unsigned DEFAULT NULL,
  `ride_color` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ride_line_daily_id` int(11) unsigned DEFAULT NULL,
  `billing_verified_by` int(11) unsigned DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `billing_adjustment` decimal(10,3) DEFAULT NULL,
  `billing_verified_status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ride_user` (`user_id`),
  KEY `fk_ride_ride_line` (`ride_line_id`),
  CONSTRAINT `fk_ride_ride_line` FOREIGN KEY (`ride_line_id`) REFERENCES `ride_line` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ride_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ride_line
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ride_line`;

CREATE TABLE `ride_line` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `borough` varchar(255) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `agency_id` int(11) unsigned DEFAULT NULL,
  `status` int(11) unsigned DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `driver_id` int(11) unsigned DEFAULT NULL,
  `vehicle_id` int(11) unsigned DEFAULT NULL,
  `ride_line_color` varchar(255) DEFAULT NULL,
  `company_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_RIDE_LINE_AGENCY` (`agency_id`),
  KEY `fk_ride_line_company` (`company_id`),
  CONSTRAINT `FK_RIDE_LINE_AGENCY` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ride_line_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ride_line_color
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ride_line_color`;

CREATE TABLE `ride_line_color` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `color_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `ride_line_color` WRITE;
/*!40000 ALTER TABLE `ride_line_color` DISABLE KEYS */;

INSERT INTO `ride_line_color` (`id`, `name`, `color_code`)
VALUES
	(1,'Aquamarine ','#7FFFD4'),
	(2,'AliceBlue ','#F0F8FF'),
	(3,'DarkGray ','#A9A9A9'),
	(4,'LawnGreen ','#7CFC00'),
	(5,'LightYellow ','#FFFFE0'),
	(6,'MintCream ','#F5FFFA'),
	(7,'YellowGreen ','#9ACD32'),
	(8,'Chocolate','#D2691E'),
	(9,'Cyan','#00FFFF'),
	(10,'Khaki ','#F0E68C'),
	(11,'Salmon ','#FA8072'),
	(12,'Gold','#FFD700'),
	(13,'Silver ','#C0C0C0'),
	(14,'Magenta','#FF00FF'),
	(15,'Olive','#808000'),
	(16,'Orange','#FFA500'),
	(17,'PaleGreen','#98FB98'),
	(18,'SkyBlue','#87CEEB'),
	(19,'Yellow','#FFFF00'),
	(20,'SeaShell ','#FFF5EE');

/*!40000 ALTER TABLE `ride_line_color` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table ride_line_daily
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ride_line_daily`;

CREATE TABLE `ride_line_daily` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `borough` varchar(255) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `driver_id` int(11) unsigned DEFAULT NULL,
  `vehicle_id` int(11) unsigned DEFAULT NULL,
  `status` int(11) unsigned DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ride_line_id` int(11) DEFAULT NULL,
  `ride_line_daily_color` varchar(255) DEFAULT NULL,
  `inbound_driver_id` int(11) DEFAULT NULL,
  `outbound_driver_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_rld_in_driver` (`inbound_driver_id`),
  KEY `fk_rld_out_driver` (`outbound_driver_id`),
  CONSTRAINT `fk_rld_in_driver` FOREIGN KEY (`inbound_driver_id`) REFERENCES `driver` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_rld_out_driver` FOREIGN KEY (`outbound_driver_id`) REFERENCES `driver` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table ride_line_has_driver
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ride_line_has_driver`;

CREATE TABLE `ride_line_has_driver` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `driver_id` int(11) DEFAULT NULL,
  `ride_line_id` int(11) unsigned DEFAULT NULL,
  `day_of_week` int(11) unsigned DEFAULT NULL,
  `need_trip` int(11) unsigned DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_rlhd_driver` (`driver_id`),
  KEY `fk_rlhd_ride_line` (`ride_line_id`),
  CONSTRAINT `fk_rlhd_driver` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_rlhd_ride_line` FOREIGN KEY (`ride_line_id`) REFERENCES `ride_line` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `level` int(11) NOT NULL,
  `level_name` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `default_visible_list` varchar(255) DEFAULT NULL,
  `default_action_list` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;

INSERT INTO `role` (`id`, `level`, `level_name`, `status`, `default_visible_list`, `default_action_list`, `updated_at`, `created_at`)
VALUES
	(1,1,'super admin',1,NULL,NULL,'2018-01-26 05:09:26','2018-01-26 05:09:26'),
	(2,2,'sub super admin',1,NULL,NULL,'2018-01-26 05:09:26','2018-01-26 05:09:26'),
	(3,3,'company admin',1,NULL,NULL,'2018-01-26 05:09:26','2018-01-26 05:09:26'),
	(4,4,'sub company admin',1,NULL,NULL,'2018-01-26 05:09:26','2018-01-26 05:09:26'),
	(5,5,'senior center admin',1,NULL,NULL,'2018-01-26 05:09:26','2018-01-26 05:09:26'),
	(6,6,'sub senior center admin',1,NULL,NULL,'2018-01-26 05:09:26','2018-01-26 05:09:26');

/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table schema_version
# ------------------------------------------------------------

DROP TABLE IF EXISTS `schema_version`;

CREATE TABLE `schema_version` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `schema_version_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table seat
# ------------------------------------------------------------

DROP TABLE IF EXISTS `seat`;

CREATE TABLE `seat` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `table_id` int(11) unsigned DEFAULT NULL,
  `seat_called` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `seat_number_capacity` int(11) unsigned DEFAULT NULL COMMENT 'how many seat does this table has',
  `seat_status` enum('AVAILABLE','UNAVAILABLE','TAKEN') DEFAULT 'AVAILABLE',
  `user_id` int(11) unsigned DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `FK_TABLE_SEAT_idx` (`table_id`),
  KEY `FK_TABLE_USER_IDX` (`user_id`),
  CONSTRAINT `fk_seat_1` FOREIGN KEY (`table_id`) REFERENCES `tables` (`id`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table service_for_client
# ------------------------------------------------------------

DROP TABLE IF EXISTS `service_for_client`;

CREATE TABLE `service_for_client` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `current_service_receiving` varchar(255) DEFAULT NULL,
  `other_comment` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `reserved_1` varchar(255) DEFAULT NULL,
  `reserved_2` varchar(255) DEFAULT NULL,
  `reserved_3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table service_plan
# ------------------------------------------------------------

DROP TABLE IF EXISTS `service_plan`;

CREATE TABLE `service_plan` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `approved_by` varchar(255) DEFAULT NULL,
  `plan_start` datetime DEFAULT NULL,
  `plan_end` datetime DEFAULT NULL,
  `days` varchar(255) DEFAULT NULL COMMENT 'Record days in week for services.',
  `doc_file_id` int(11) unsigned DEFAULT NULL COMMENT 'refers the file_upload_id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` int(11) unsigned DEFAULT NULL,
  `medical_status` varchar(255) DEFAULT NULL,
  `nutrition_status` varchar(255) DEFAULT NULL,
  `sensory_status` varchar(255) DEFAULT NULL,
  `medication_status` varchar(255) DEFAULT NULL,
  `pain_status` varchar(255) DEFAULT NULL,
  `congonitive_status` varchar(255) DEFAULT NULL,
  `psychosocial_status` varchar(255) DEFAULT NULL,
  `spiritual_status` varchar(255) DEFAULT NULL,
  `communication_status` varchar(255) DEFAULT NULL,
  `expected_outcome` varchar(255) DEFAULT NULL,
  `outcome_criteria` varchar(255) DEFAULT NULL,
  `outcome_target_date` timestamp NULL DEFAULT NULL,
  `outcome_date_achieved` timestamp NULL DEFAULT NULL,
  `activity_and_engagement_level` varchar(255) DEFAULT NULL,
  `capacity_self_estimate` varchar(255) DEFAULT NULL,
  `adls_level_care` varchar(255) DEFAULT NULL,
  `capacity_independence_self_care` varchar(255) DEFAULT NULL,
  `signature_id` int(11) unsigned DEFAULT NULL COMMENT 'refers the table of signature',
  `insurance_start` datetime DEFAULT NULL,
  `insurance_end` datetime DEFAULT NULL,
  `authorization_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_serviceplan_user1_idx` (`user_id`),
  KEY `fk_service_plan_1_idx` (`doc_file_id`),
  KEY `fk_service_plan_3_idx` (`signature_id`),
  CONSTRAINT `fk_service_plan_1` FOREIGN KEY (`doc_file_id`) REFERENCES `file_upload` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_service_plan_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_service_plan_3` FOREIGN KEY (`signature_id`) REFERENCES `signature` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table signature
# ------------------------------------------------------------

DROP TABLE IF EXISTS `signature`;

CREATE TABLE `signature` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `signed_by` varchar(255) NOT NULL COMMENT 'signed by nurse/translater/patient/senior, etc.',
  `signed_date_time` datetime DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `signature_file_id` int(11) unsigned NOT NULL COMMENT 'refers the file_upload_id in the table file_upload',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_signature _signature_file1_idx` (`signature_file_id`),
  CONSTRAINT `fk_signature _signature_file1` FOREIGN KEY (`signature_file_id`) REFERENCES `file_upload` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tables
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tables`;

CREATE TABLE `tables` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `agency_id` int(11) unsigned DEFAULT NULL,
  `table_called` varchar(255) DEFAULT NULL COMMENT 'could be table A/B/C/D,or 1/2/3/4, etc.',
  `table_capacity` int(11) unsigned DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_agency_table_idx` (`agency_id`),
  CONSTRAINT `fk_table_agency_id` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table todolist
# ------------------------------------------------------------

DROP TABLE IF EXISTS `todolist`;

CREATE TABLE `todolist` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) unsigned DEFAULT NULL,
  `num` varchar(255) DEFAULT NULL,
  `content` varchar(255) NOT NULL,
  `priority` varchar(255) NOT NULL,
  `color` varchar(255) DEFAULT '#000000',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_todlolist_1_idx` (`admin_id`),
  CONSTRAINT `fk_todlolist_1` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table training
# ------------------------------------------------------------

DROP TABLE IF EXISTS `training`;

CREATE TABLE `training` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `trainer` varchar(255) DEFAULT NULL,
  `location` varchar(255) NOT NULL,
  `note` varchar(1000) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL,
  `trainee` varchar(255) DEFAULT NULL,
  `agency_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table training_has_employee
# ------------------------------------------------------------

DROP TABLE IF EXISTS `training_has_employee`;

CREATE TABLE `training_has_employee` (
  `training_id` int(11) unsigned NOT NULL,
  `employee_id` int(11) unsigned NOT NULL,
  `notes` varchar(1000) DEFAULT NULL,
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `fk_training_has_employee_employee1_idx` (`employee_id`),
  KEY `fk_training_has_employee_training1_idx` (`training_id`),
  CONSTRAINT `fk_training_has_employee_1` FOREIGN KEY (`training_id`) REFERENCES `training` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_training_has_employee_2` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table trip_analyze
# ------------------------------------------------------------

DROP TABLE IF EXISTS `trip_analyze`;

CREATE TABLE `trip_analyze` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `driver_id` int(11) DEFAULT NULL,
  `ride_line_id` int(11) DEFAULT NULL,
  `analyze_date` date NOT NULL,
  `user_full_name` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `max_trip_id` int(11) NOT NULL DEFAULT '0',
  `pickup_zip` varchar(255) DEFAULT NULL,
  `dropoff_zip` varchar(255) DEFAULT NULL,
  `trip_count` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `company_id` int(11) unsigned DEFAULT NULL,
  `agency_id` int(11) unsigned DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) NOT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `social_security_number` varchar(255) DEFAULT NULL,
  `dob` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) NOT NULL,
  `secondary_phone` varchar(255) DEFAULT NULL,
  `verification_code` varchar(255) DEFAULT NULL,
  `address_type` varchar(255) DEFAULT NULL,
  `address_one` varchar(255) DEFAULT NULL,
  `address_two` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zipcode` varchar(255) DEFAULT NULL,
  `emergency_contact_first_name` varchar(255) DEFAULT NULL,
  `emergency_contact_middle_name` varchar(255) DEFAULT NULL,
  `emergency_contact_last_name` varchar(255) DEFAULT NULL,
  `relationship_to_paticipant` varchar(255) DEFAULT NULL,
  `emergency_contact_phone` varchar(255) DEFAULT NULL,
  `emergency_contact_address_one` varchar(255) DEFAULT NULL,
  `emergency_contact_address_two` varchar(255) DEFAULT NULL,
  `emergency_contact_city` varchar(255) DEFAULT NULL,
  `emergency_contact_state` varchar(255) DEFAULT NULL,
  `emergency_contact_zipcode` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `preferred_meal_id` int(11) unsigned DEFAULT NULL,
  `preferred_activity_id` int(11) unsigned DEFAULT NULL,
  `preferred_seat_id` int(11) unsigned DEFAULT NULL,
  `approvable_mail` int(11) DEFAULT NULL,
  `medicaid_no` varchar(255) DEFAULT NULL,
  `medicare_no` varchar(255) DEFAULT NULL,
  `insurance_id` varchar(255) DEFAULT NULL,
  `insurance_eligiable` varchar(255) DEFAULT NULL,
  `authorization_start` datetime DEFAULT NULL,
  `authorization_end` datetime DEFAULT NULL,
  `eligiable_start` datetime DEFAULT NULL,
  `eligiable_end` datetime DEFAULT NULL,
  `family_doctor_id` int(11) unsigned DEFAULT NULL,
  `expert_doctor_id` int(11) unsigned DEFAULT NULL COMMENT 'refer the table organization for doctor',
  `medical_condition` varchar(1000) DEFAULT NULL,
  `status` enum('PENDING','REGISTERED','ACTIVE','INACTIVE') DEFAULT 'REGISTERED',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status_second` enum('PENDING','REGISTERED','ACTIVE','INACTIVE') DEFAULT 'REGISTERED',
  `remember_token` varchar(255) DEFAULT NULL,
  `role_id` int(11) unsigned DEFAULT NULL,
  `profile_photo_id` int(11) unsigned DEFAULT NULL,
  `pin` varchar(255) DEFAULT NULL,
  `barcode` varchar(255) DEFAULT NULL,
  `box_number` varchar(255) DEFAULT NULL,
  `add_by_admin_id` int(11) unsigned DEFAULT NULL COMMENT 'This user is added by admin have admin id = ',
  `recommended_by_employee_id` int(11) unsigned DEFAULT NULL COMMENT 'This user is recommended by employee have id = ',
  `authorization_code` varchar(45) DEFAULT NULL,
  `days_in_week` varchar(45) DEFAULT NULL,
  `citizenship` enum('US_CITIZEN','GREEN_CARD','NA') DEFAULT 'US_CITIZEN',
  `education` enum('PRIMARY_SCHOOL','MIDDLE_SCHOOL','HIGH_SCHOOL','COLLEAGUE','MASTER','PHD') DEFAULT 'COLLEAGUE',
  `work_background` varchar(450) DEFAULT NULL,
  `recreation_hobbies_skills` varchar(450) DEFAULT NULL,
  `religious_preference` varchar(250) DEFAULT NULL,
  `children` int(10) DEFAULT '0',
  `authorization_file_id` varchar(1024) DEFAULT NULL,
  `service_plan_file_id` varchar(1024) DEFAULT NULL,
  `medication_id` int(11) unsigned DEFAULT NULL,
  `preferred_table_id` int(11) unsigned DEFAULT NULL,
  `preferred_training_id` int(11) unsigned DEFAULT NULL,
  `case_manager_id` int(11) unsigned DEFAULT NULL COMMENT 'refer the employee_id in the table of the employee',
  `rules_and_regus_doc_id` int(11) unsigned DEFAULT NULL,
  `fixed_status` enum('FIXED','UNFIXED') DEFAULT NULL,
  `need_trip` int(11) unsigned DEFAULT NULL,
  `period_type` int(11) DEFAULT NULL,
  `maximum_units` int(11) DEFAULT NULL,
  `unit_type` int(11) DEFAULT NULL,
  `auth_week_start` int(11) DEFAULT NULL,
  `ride_line_id` int(11) unsigned DEFAULT NULL,
  `addr_lat` varchar(45) DEFAULT NULL,
  `addr_lng` varchar(45) DEFAULT NULL,
  `vacation_start` datetime DEFAULT NULL,
  `vacation_end` datetime DEFAULT NULL,
  `service_plan_id` int(11) DEFAULT NULL,
  `assessment_duration` int(11) DEFAULT NULL,
  `assessment_start_date` datetime DEFAULT NULL,
  `note` varchar(1000) DEFAULT NULL,
  `blacklist_user` varchar(1000) DEFAULT NULL,
  `blacklist_driver` varchar(1000) DEFAULT NULL,
  `visit_price` double(16,2) DEFAULT NULL,
  `trip_price` double(16,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_last_name` (`agency_id`),
  KEY `fk_user_agency1_idx` (`agency_id`),
  KEY `fk_admin_role1_idx` (`role_id`),
  KEY `fk_rules_and_regus_doc_fk_idx` (`rules_and_regus_doc_id`),
  KEY `fk_user_company_fk_idx` (`company_id`),
  KEY `fk_user_activity_fk_idx` (`preferred_activity_id`),
  KEY `fk_user_family_doctor_fk_idx` (`family_doctor_id`),
  KEY `fk_user_expert_doctor_fk_idx` (`expert_doctor_id`),
  KEY `fk_user_added_by_admin_fk_idx` (`add_by_admin_id`),
  KEY `fk_user_recommended_by_employee_fk_idx` (`recommended_by_employee_id`),
  KEY `fk_user_table_fk_idx` (`preferred_table_id`),
  KEY `fk_user_training_fk_idx` (`preferred_training_id`),
  KEY `fk_user_case_manager_id_idx` (`case_manager_id`),
  KEY `fk_user_meal_fk` (`preferred_meal_id`),
  KEY `fk_user_seat_fk` (`preferred_seat_id`),
  KEY `fk_user_profile_picture_fk` (`profile_photo_id`),
  KEY `FK_RIDE_LINE` (`ride_line_id`),
  CONSTRAINT `FK_RIDE_LINE` FOREIGN KEY (`ride_line_id`) REFERENCES `ride_line` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_rules_and_regus_doc_fk` FOREIGN KEY (`rules_and_regus_doc_id`) REFERENCES `file_upload` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_activity_fk` FOREIGN KEY (`preferred_activity_id`) REFERENCES `activity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_added_by_admin_fk` FOREIGN KEY (`add_by_admin_id`) REFERENCES `admin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_agency_fk` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_case_manager_id` FOREIGN KEY (`case_manager_id`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_company_fk` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_expert_doctor_fk` FOREIGN KEY (`expert_doctor_id`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_family_doctor_fk` FOREIGN KEY (`family_doctor_id`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_meal_fk` FOREIGN KEY (`preferred_meal_id`) REFERENCES `meal` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_profile_picture_fk` FOREIGN KEY (`profile_photo_id`) REFERENCES `file_upload` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_recommended_by_employee_fk` FOREIGN KEY (`recommended_by_employee_id`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_role_fk` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_seat_fk` FOREIGN KEY (`preferred_seat_id`) REFERENCES `seat` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_table_fk` FOREIGN KEY (`preferred_table_id`) REFERENCES `tables` (`id`),
  CONSTRAINT `fk_user_training_fk` FOREIGN KEY (`preferred_training_id`) REFERENCES `training` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table storing all customers. Holds foreign keys to the address table and the store table where this customer is registered.\n\nBasic information about the customer like first and last name are stored in the table itself. Same for the date the record was created and when the information was last updated.';



# Dump of table user_has_food_allergies
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_has_food_allergies`;

CREATE TABLE `user_has_food_allergies` (
  `user_id` int(11) NOT NULL,
  `food_allergies_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`user_id`,`food_allergies_id`),
  KEY `fk_user_has_food_allergies` (`food_allergies_id`),
  CONSTRAINT `fk_user_has_food_allergies` FOREIGN KEY (`food_allergies_id`) REFERENCES `food_allergy` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table vehicle
# ------------------------------------------------------------

DROP TABLE IF EXISTS `vehicle`;

CREATE TABLE `vehicle` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `agency_id` int(11) unsigned DEFAULT NULL,
  `profile_photo_id` int(11) unsigned DEFAULT NULL,
  `vehicle_type_id` int(11) DEFAULT NULL,
  `vehicle_brand_id` int(11) DEFAULT NULL,
  `driver_id` varchar(255) DEFAULT NULL,
  `cab_type` varchar(255) DEFAULT NULL,
  `year` int(4) NOT NULL,
  `vin` varchar(255) NOT NULL,
  `model` varchar(255) NOT NULL,
  `vehicle_license_state` varchar(255) NOT NULL,
  `vehicle_plate_num` varchar(255) NOT NULL,
  `vehicle_registration` varchar(255) DEFAULT NULL,
  `vehicle_registration_start` date NOT NULL,
  `vehicle_registration_expire` date NOT NULL,
  `vehicle_registration_status` int(1) NOT NULL,
  `liability_insurance` varchar(255) DEFAULT NULL,
  `liability_amount` int(11) DEFAULT NULL,
  `liability_ecarride_certificate_holder` int(1) DEFAULT NULL,
  `liability_ecarride_additional_insured` int(1) DEFAULT NULL,
  `liability_self_insured` int(1) DEFAULT NULL,
  `liability_name_insurer` varchar(255) DEFAULT NULL,
  `liability_insurance_start` date DEFAULT NULL,
  `liability_insurance_expire` date DEFAULT NULL,
  `liability_insurance_status` int(1) DEFAULT NULL,
  `extra_insurance` varchar(255) DEFAULT NULL,
  `extra_insurance_start` date DEFAULT NULL,
  `extra_insurance_expire` date DEFAULT NULL,
  `extra_insurance_status` int(1) DEFAULT '0',
  `vehicle_tlc_fhv_license` varchar(255) DEFAULT NULL,
  `vehicle_tlc_fhv_license_num` varchar(255) DEFAULT NULL,
  `vehicle_tlc_fhv_license_start` date DEFAULT NULL,
  `vehicle_tlc_fhv_license_expire` date DEFAULT NULL,
  `vehicle_tlc_fhv_license_status` int(1) DEFAULT NULL,
  `vehicle_inspection` varchar(255) DEFAULT NULL,
  `vehicle_inspection_start` date DEFAULT NULL,
  `vehicle_inspection_expire` date DEFAULT NULL,
  `vehicle_inspection_status` int(1) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int(1) DEFAULT '0',
  `allow_wheelchair` int(1) DEFAULT '0',
  `allow_stretcher` int(1) DEFAULT '0',
  `is_ambulatory` int(1) DEFAULT '0',
  `color` varchar(32) DEFAULT 'black',
  PRIMARY KEY (`id`),
  KEY `fk_vehicle_agency_id_idx` (`agency_id`),
  KEY `fk_profile_photo_id_idx` (`profile_photo_id`),
  KEY `fk_vehicle_type_id_idx` (`vehicle_type_id`),
  KEY `fk_vehicle_brand_id_idx` (`vehicle_brand_id`),
  CONSTRAINT `fk_agency_id` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_profile_photo_id` FOREIGN KEY (`profile_photo_id`) REFERENCES `file_upload` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_vehicle_brand_id` FOREIGN KEY (`vehicle_brand_id`) REFERENCES `vehicle_brand` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_vehicle_type_id` FOREIGN KEY (`vehicle_type_id`) REFERENCES `vehicle_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table vehicle_brand
# ------------------------------------------------------------

DROP TABLE IF EXISTS `vehicle_brand`;

CREATE TABLE `vehicle_brand` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `brand_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table vehicle_type
# ------------------------------------------------------------

DROP TABLE IF EXISTS `vehicle_type`;

CREATE TABLE `vehicle_type` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `capacity` int(11) NOT NULL,
  `status` int(1) NOT NULL,
  `image_url` varchar(255) NOT NULL DEFAULT 'default.png',
  `vehicle_icon` varchar(255) NOT NULL DEFAULT 'default.png',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table visit
# ------------------------------------------------------------

DROP TABLE IF EXISTS `visit`;

CREATE TABLE `visit` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `agency_id` int(11) unsigned NOT NULL,
  `check_in_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `selected_seat` varchar(255) DEFAULT NULL,
  `pin` varchar(255) NOT NULL,
  `check_out_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `user_comments` varchar(1000) DEFAULT NULL,
  `notes` varchar(1000) DEFAULT NULL,
  `status` varchar(48) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` int(11) unsigned NOT NULL,
  `service_plan_id` int(11) unsigned DEFAULT NULL,
  `signature` varchar(255) DEFAULT NULL,
  `check_in_signature_id` int(11) unsigned DEFAULT NULL,
  `check_out_signature_id` int(11) unsigned DEFAULT NULL,
  `active` int(11) DEFAULT NULL,
  `reservation_id` int(11) unsigned DEFAULT NULL,
  `billing_code` varchar(45) DEFAULT NULL,
  `expected_money` decimal(10,3) DEFAULT NULL,
  `actual_money` decimal(10,3) DEFAULT NULL,
  `selected_lunch_id` varchar(255) DEFAULT NULL,
  `selected_breakfast_id` varchar(255) DEFAULT NULL,
  `selected_dinner_id` varchar(255) DEFAULT NULL,
  `admin_id` int(11) unsigned DEFAULT NULL,
  `seat_id` int(11) unsigned DEFAULT NULL,
  `check_in_photo_id` int(11) unsigned DEFAULT NULL COMMENT 'refer the file_upload_id of table file_upload',
  `check_out_photo_id` int(11) unsigned DEFAULT NULL COMMENT 'refer the file_upload_id of table file_upload',
  `activity_id` int(11) DEFAULT NULL,
  `reserved_date` datetime DEFAULT NULL,
  `billing_verified_status` varchar(255) DEFAULT NULL,
  `billing_adjustment` decimal(10,3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `billing_code_UNIQUE` (`billing_code`),
  KEY `fk_visit_1_idx` (`user_id`),
  KEY `fk_visit_2_idx` (`agency_id`),
  KEY `fk_visit_3_idx` (`service_plan_id`),
  KEY `fk_visit_5_idx` (`check_out_signature_id`),
  KEY `fk_visit_6_idx` (`check_in_photo_id`),
  KEY `fk_visit_7_idx` (`check_out_photo_id`),
  KEY `fk_visit_9_idx` (`admin_id`),
  KEY `fk_visit_10_idx` (`seat_id`),
  KEY `fk_visit_11_idx` (`reservation_id`),
  KEY `fk_visit_4` (`check_in_signature_id`),
  CONSTRAINT `fk_visit_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_visit_10` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_visit_11` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_visit_2` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_visit_3` FOREIGN KEY (`service_plan_id`) REFERENCES `service_plan` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_visit_6` FOREIGN KEY (`check_in_photo_id`) REFERENCES `file_upload` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_visit_7` FOREIGN KEY (`check_out_photo_id`) REFERENCES `file_upload` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_visit_9` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table visit_has_activity
# ------------------------------------------------------------

DROP TABLE IF EXISTS `visit_has_activity`;

CREATE TABLE `visit_has_activity` (
  `visit_id` int(11) unsigned NOT NULL,
  `activity_id` int(11) unsigned NOT NULL,
  KEY `fk_visit_has_activity_visit_idx` (`visit_id`),
  KEY `fk_visit_has_activity_activity_idx` (`activity_id`),
  CONSTRAINT `fk_visit_has_activity_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_visit_has_activity_visit` FOREIGN KEY (`visit_id`) REFERENCES `visit` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table work_item
# ------------------------------------------------------------

DROP TABLE IF EXISTS `work_item`;

CREATE TABLE `work_item` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `item_name` varchar(255) NOT NULL,
  `item_note` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' help on cooking, help on shopping, help on laundry, etc.';




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
