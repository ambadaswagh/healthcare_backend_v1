/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.6.36 : Database - healthcare
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`healthcare` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `healthcare`;

/*Table structure for table `activity` */

DROP TABLE IF EXISTS `activity`;

CREATE TABLE `activity` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `status` int(11) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `instructor_employee_id` int(10) unsigned NOT NULL,
  `time_start` varchar(255) DEFAULT NULL,
  `time_end` varchar(255) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `note` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `activity` */

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
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
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL,
  `role_id` int(10) unsigned NOT NULL,
  `menu_list` varchar(255) DEFAULT NULL,
  `profile_photo` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_username_admin` (`username`),
  KEY `fk_admin_role1_idx` (`role_id`),
  KEY `fk_profile_photoadmin_idkey2` (`profile_photo`),
  CONSTRAINT `fk_admin_role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `fk_profile_photoadmin_idkey2` FOREIGN KEY (`profile_photo`) REFERENCES `file_upload` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `admin` */

/*Table structure for table `admin_post` */

DROP TABLE IF EXISTS `admin_post`;

CREATE TABLE `admin_post` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `post_text` varchar(1000) NOT NULL,
  `post_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL,
  `admin_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_admin_post_admin1_idx` (`admin_id`),
  CONSTRAINT `fk_admin_post_admin1` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `admin_post` */

/*Table structure for table `agency` */

DROP TABLE IF EXISTS `agency`;

CREATE TABLE `agency` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `license_no` varchar(255) NOT NULL,
  `company_id` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `tracking_mode` int(11) NOT NULL,
  `contact_person` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `address_one` varchar(255) NOT NULL,
  `address_two` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `zipcode` varchar(255) NOT NULL,
  `timezone` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `holiday` varchar(1000) NOT NULL,
  `fax` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `company_id1` int(10) unsigned NOT NULL,
  `agency_type_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_agency_company1_idx` (`company_id1`),
  KEY `fk_agency_agency_type1_idx` (`agency_type_id`),
  CONSTRAINT `fk_agency_agency_type1` FOREIGN KEY (`agency_type_id`) REFERENCES `agency_type` (`id`),
  CONSTRAINT `fk_agency_company1` FOREIGN KEY (`company_id1`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `agency` */

/*Table structure for table `agency_type` */

DROP TABLE IF EXISTS `agency_type`;

CREATE TABLE `agency_type` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `status` int(11) NOT NULL COMMENT 'type status',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `agency_type` */

/*Table structure for table `caregiver` */

DROP TABLE IF EXISTS `caregiver`;

CREATE TABLE `caregiver` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `company_id` int(10) unsigned DEFAULT NULL,
  `agency_id` int(10) unsigned DEFAULT NULL,
  `caregiver_type` int(11) NOT NULL,
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
  `certificate` varchar(255) NOT NULL,
  `certificate_start` datetime NOT NULL,
  `certificate_end` datetime NOT NULL,
  `status` int(11) DEFAULT NULL,
  `vacation_note` varchar(1000) DEFAULT NULL,
  `vacation_start` datetime DEFAULT NULL,
  `vacation_end` datetime DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `profile_photo` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_caregiver_agency` (`agency_id`),
  KEY `idx_caregiver_company` (`company_id`),
  KEY `fk_profile_photocaregiver_idkey2` (`profile_photo`),
  CONSTRAINT `fk_caregiver_agency` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`),
  CONSTRAINT `fk_caregiver_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `fk_profile_photocaregiver_idkey2` FOREIGN KEY (`profile_photo`) REFERENCES `file_upload` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table storing all customers. Holds foreign keys to the address table and the store table where this customer is registered.\n\nBasic information about the customer like first and last name are stored in the table itself. Same for the date the record was created and when the information was last updated.';

/*Data for the table `caregiver` */

/*Table structure for table `company` */

DROP TABLE IF EXISTS `company`;

CREATE TABLE `company` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
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
  `address_two` varchar(255) NOT NULL,
  `worktime_start` time NOT NULL DEFAULT '00:00:00',
  `worktime_end` time NOT NULL DEFAULT '23:59:59',
  `city` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `zipcode` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL,
  `days_work` varchar(255) DEFAULT NULL COMMENT 'Mon, Tue, Wed, Thu, Fri, Sat, Sun.',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `company` */

/*Table structure for table `content` */

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
  `base_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `fld_accessNo` (`access_key`),
  KEY `fk_parent_key_content1` (`parent_id`),
  CONSTRAINT `fk_parent_key_content1` FOREIGN KEY (`parent_id`) REFERENCES `content` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8;

/*Data for the table `content` */

/*Table structure for table `employee` */

DROP TABLE IF EXISTS `employee`;

CREATE TABLE `employee` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `gender` varchar(255) NOT NULL,
  `social_security_number` varchar(255) NOT NULL,
  `date_of_birth` datetime NOT NULL,
  `physical_exam` varchar(255) NOT NULL,
  `certificate_name` varchar(255) DEFAULT NULL,
  `certificate_start` datetime DEFAULT NULL,
  `certificate_end` datetime DEFAULT NULL,
  `work_start` datetime NOT NULL,
  `work_end` datetime DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `manager` varchar(255) NOT NULL COMMENT 'could be part-time, full-time, consultant, volunteer, etc.',
  `type` varchar(255) NOT NULL COMMENT 'could be part-time, full-time, consultant, volunteer, etc.',
  `status` varchar(255) DEFAULT NULL,
  `background_check` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `agency_id` int(10) unsigned NOT NULL,
  `weekly_hours_worked` varchar(10) DEFAULT '0h0m',
  PRIMARY KEY (`id`),
  KEY `fk_employee_agency1_idx` (`agency_id`),
  CONSTRAINT `fk_employee_agency1` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `employee` */

/*Table structure for table `employee_clocking` */

DROP TABLE IF EXISTS `employee_clocking`;

CREATE TABLE `employee_clocking` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `employee_id` int(10) unsigned NOT NULL,
  `check_in_time` datetime NOT NULL,
  `check_in_signature` varchar(255) DEFAULT NULL,
  `check_out_time` datetime DEFAULT NULL,
  `check_out_signature` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `checkin_signature_Id` int(10) unsigned DEFAULT NULL,
  `checkout_signature_Id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_employee_clocking_idx` (`employee_id`),
  KEY `fk_employeeclocking_key1` (`checkin_signature_Id`),
  KEY `fk_employeeclocking_key2` (`checkout_signature_Id`),
  CONSTRAINT `fk_employee_clocking` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `fk_employeeclocking_key1` FOREIGN KEY (`checkin_signature_Id`) REFERENCES `file_upload` (`id`),
  CONSTRAINT `fk_employeeclocking_key2` FOREIGN KEY (`checkout_signature_Id`) REFERENCES `file_upload` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `employee_clocking` */

/*Table structure for table `employee_has_activity` */

DROP TABLE IF EXISTS `employee_has_activity`;

CREATE TABLE `employee_has_activity` (
  `employee_id` int(10) unsigned NOT NULL,
  `activity_id` int(10) unsigned NOT NULL,
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `fk_employee_has_activity_activity1_idx` (`activity_id`),
  KEY `fk_employee_has_activity_employee1_idx` (`employee_id`),
  CONSTRAINT `fk_employee_has_activity_activity1` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`),
  CONSTRAINT `fk_employee_has_activity_employee1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `employee_has_activity` */

/*Table structure for table `file_upload` */

DROP TABLE IF EXISTS `file_upload`;

CREATE TABLE `file_upload` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `entity` varchar(255) NOT NULL COMMENT 'a entity name that such document belong',
  `entity_id` int(10) unsigned NOT NULL COMMENT 'an id id row entity that such document belong',
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

/*Data for the table `file_upload` */

/*Table structure for table `health_insurance_claim` */

DROP TABLE IF EXISTS `health_insurance_claim`;

CREATE TABLE `health_insurance_claim` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `insurance_plan_name` varchar(255) NOT NULL COMMENT 'Could be MEDICARE , MEDICAID, TRICARE-CHAMPUS, CHAMPVA, GROUP-HEALTH-PLAN, FECA-BLKLUNG, OTHER',
  `relationship_to_insured` varchar(255) NOT NULL COMMENT 'Could be Self , Spouse, Child, Other',
  `patient_marital_status` varchar(255) NOT NULL COMMENT 'Could be Single, Married, Other',
  `patient_employment_status` varchar(255) NOT NULL COMMENT 'Could be Employed, Full-time Student, Part-time Student',
  `other_insured_first_name` varchar(255) DEFAULT NULL,
  `other_insured_middle_name` varchar(255) DEFAULT NULL,
  `other_insured_last_name` varchar(255) DEFAULT NULL,
  `other_insured_policy_or_group_no` varchar(255) DEFAULT NULL,
  `other_insured_dob` datetime DEFAULT NULL,
  `other_insured_sex` varchar(255) DEFAULT NULL COMMENT 'Could be Male, Female',
  `other_insured_employer_school_name` varchar(255) DEFAULT NULL,
  `other_insured_insurance_insurance_plan_name` varchar(255) DEFAULT NULL COMMENT 'Could be MEDICARE , MEDICAID, TRICARE-CHAMPUS, CHAMPVA, GROUP-HEALTH-PLAN, FECA-BLKLUNG, OTHER',
  `patient_condtion_relation` varchar(255) NOT NULL COMMENT 'Could be EMPLOYMENT , AUTO ACCIDENT, OTHER ACCIDENT',
  `auto_accident_place` varchar(255) DEFAULT NULL COMMENT 'Value will be exist if patient_condition_relation is ''Auto Accident''',
  `reserved_local_use` varchar(255) NOT NULL,
  `patient_signature` int(11) unsigned NOT NULL,
  `signature_date` date NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` int(10) unsigned NOT NULL,
  `insured_id_no` varchar(255) NOT NULL,
  `insured_first_name` varchar(255) NOT NULL,
  `insured_middle_name` varchar(255) DEFAULT NULL,
  `insured_last_name` varchar(255) NOT NULL,
  `insured_address` varchar(255) NOT NULL,
  `insured_city` varchar(255) NOT NULL,
  `insured_state` varchar(255) NOT NULL,
  `insured_zipcode` varchar(255) NOT NULL,
  `insured_phone_no` varchar(255) NOT NULL,
  `insured_policy_group_or_feca_no` varchar(255) NOT NULL,
  `insured_dob` datetime NOT NULL,
  `insured_sex` varchar(255) NOT NULL COMMENT 'Could be Male, Female',
  `insured_employer_school_name` varchar(255) DEFAULT NULL,
  `insured_insurance_plan_name_or_program_name` varchar(255) DEFAULT NULL COMMENT 'Could be MEDICARE , MEDICAID, TRICARE-CHAMPUS, CHAMPVA, GROUP-HEALTH-PLAN, FECA-BLKLUNG, OTHER',
  `insured_another_health_plan` tinyint(1) NOT NULL,
  `insured_signature` int(11) unsigned NOT NULL,
  `current_illness_injury_pregnancy_date` date NOT NULL,
  `first_similar_illness_date` date DEFAULT NULL,
  `patient_unable_work_current_occupation_from_date` date NOT NULL,
  `patient_unable_work_current_occupation_from_to` date NOT NULL,
  `hospitalization_related_to_current_services_date_from` date NOT NULL,
  `hospitalization_related_to_current_services_date_to` date NOT NULL,
  `outside_lab` tinyint(1) NOT NULL,
  `charges` decimal(10,3) NOT NULL,
  `medicaid_resubmission_code` varchar(255) NOT NULL,
  `original_ref_no` varchar(255) NOT NULL,
  `prior_authorization_no` varchar(255) NOT NULL,
  `referring_provider_name` varchar(255) NOT NULL,
  `referring_provider_npi` varchar(255) NOT NULL,
  `reserved_for_local_use` varchar(255) NOT NULL,
  `diagnosis_or_nature_of_illness_or_injury_type_1` varchar(255) DEFAULT NULL,
  `diagnosis_or_nature_of_illness_or_injury_type_2` varchar(255) DEFAULT NULL,
  `diagnosis_or_nature_of_illness_or_injury_type_3` varchar(255) DEFAULT NULL,
  `diagnosis_or_nature_of_illness_or_injury_type_4` varchar(255) DEFAULT NULL,
  `federal_tax_id_no_ssn` varchar(255) NOT NULL,
  `federal_tax_id_no_ein` varchar(255) NOT NULL,
  `patient_account_no` varchar(255) NOT NULL,
  `accept_assignment` tinyint(1) NOT NULL,
  `total_charge` decimal(10,3) NOT NULL,
  `amount_paid` decimal(10,3) NOT NULL,
  `balance_due` decimal(10,3) NOT NULL,
  `physician_or_supplier_signature` int(11) unsigned NOT NULL,
  `physician_or_supplier_signature_date` date NOT NULL,
  `service_facility_location_information_a` varchar(255) NOT NULL,
  `service_facility_location_information_b` varchar(255) NOT NULL,
  `billing_provider_info_and_ph_no_a` varchar(255) NOT NULL,
  `billing_provider_info_and_ph_no_b` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_insurance_claim1` (`user_id`),
  KEY `fk_health_insurance_claim_file_upload_patient_signature` (`patient_signature`),
  KEY `fk_health_insurance_claim_file_upload_insured_signature` (`insured_signature`),
  KEY `fk_health_insurance_claim_doc_phy_or_sup_sign` (`physician_or_supplier_signature`),
  CONSTRAINT `fk_health_insurance_claim_doc_phy_or_sup_sign` FOREIGN KEY (`physician_or_supplier_signature`) REFERENCES `file_upload` (`id`),
  CONSTRAINT `fk_health_insurance_claim_file_upload_insured_signature` FOREIGN KEY (`insured_signature`) REFERENCES `file_upload` (`id`),
  CONSTRAINT `fk_health_insurance_claim_file_upload_patient_signature` FOREIGN KEY (`patient_signature`) REFERENCES `file_upload` (`id`),
  CONSTRAINT `fk_user_insurance_claim1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table structure for table `health_insurance_claim`';

/*Data for the table `health_insurance_claim` */

/*Table structure for table `home_visit` */

DROP TABLE IF EXISTS `home_visit`;

CREATE TABLE `home_visit` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `check_in_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `carereceiver_signature` varchar(255) DEFAULT NULL,
  `check_out_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `carereceiver_comments` varchar(1000) DEFAULT NULL,
  `notes` varchar(1000) DEFAULT NULL,
  `status` varchar(48) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `serviceplan_id` int(10) unsigned DEFAULT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `caregiver_id` int(10) unsigned NOT NULL,
  `worked_items` varchar(255) DEFAULT NULL,
  `carereceiver_signature_Id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_home_visit_serviceplan` (`serviceplan_id`),
  KEY `idx_home_visit_user` (`user_id`),
  KEY `idx_home_visit_caregiver` (`caregiver_id`),
  KEY `fk_homevisit_sign_key2` (`carereceiver_signature_Id`),
  CONSTRAINT `fk_home_visit_caregiver` FOREIGN KEY (`caregiver_id`) REFERENCES `caregiver` (`id`),
  CONSTRAINT `fk_home_visit_serviceplan` FOREIGN KEY (`serviceplan_id`) REFERENCES `serviceplan` (`id`),
  CONSTRAINT `fk_home_visit_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_homevisit_sign_key2` FOREIGN KEY (`carereceiver_signature_Id`) REFERENCES `file_upload` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `home_visit` */

/*Table structure for table `language` */

DROP TABLE IF EXISTS `language`;

CREATE TABLE `language` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(5) NOT NULL,
  `country` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='internalization and multi language support';

/*Data for the table `language` */

/*Table structure for table `meal` */

DROP TABLE IF EXISTS `meal`;

CREATE TABLE `meal` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `meal_class` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `ingredients` varchar(1000) DEFAULT NULL,
  `notes` varchar(1000) DEFAULT NULL,
  `verified_by_nutritionist` int(11) DEFAULT '0',
  `status` int(11) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `visit_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_meal_visit1_idx` (`visit_id`),
  CONSTRAINT `fk_meal_visit1` FOREIGN KEY (`visit_id`) REFERENCES `visit` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `meal` */

/*Table structure for table `menu` */

DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `angular_url` varchar(255) NOT NULL,
  `page` varchar(255) NOT NULL,
  `class` varchar(255) NOT NULL,
  `img_url` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `display_order` int(11) DEFAULT '0',
  `status` int(11) DEFAULT '1',
  `role_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_menu_role1_idx` (`role_id`),
  CONSTRAINT `fk_menu_role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `menu` */

/*Table structure for table `patient_diagnosis` */

DROP TABLE IF EXISTS `patient_diagnosis`;

CREATE TABLE `patient_diagnosis` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `patient_date_of_service_from` date NOT NULL,
  `patient_date_of_service_to` date NOT NULL,
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
  PRIMARY KEY (`id`),
  KEY `fk_patient_diagnosis_health_insurance_claim` (`health_insurance_claim_id`),
  CONSTRAINT `fk_patient_diagnosis_health_insurance_claim` FOREIGN KEY (`health_insurance_claim_id`) REFERENCES `health_insurance_claim` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table structure for table `patient_diagnosis`';

/*Data for the table `patient_diagnosis` */

/*Table structure for table `report` */

DROP TABLE IF EXISTS `report`;

CREATE TABLE `report` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `base_id` int(10) unsigned NOT NULL,
  `company_id` int(10) unsigned NOT NULL,
  `admin_id` int(10) unsigned NOT NULL,
  `report_title` varchar(255) NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `data_columns` varchar(255) NOT NULL,
  `format` varchar(255) DEFAULT NULL,
  `download_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `role_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_report_role1_idx` (`role_id`),
  CONSTRAINT `fk_report_role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `report` */

/*Table structure for table `review` */

DROP TABLE IF EXISTS `review`;

CREATE TABLE `review` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `employee_id` int(10) unsigned NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `health_condition` varchar(9000) DEFAULT NULL,
  `card_pulm_condition` varchar(1000) DEFAULT NULL,
  `activity_details` varchar(1000) DEFAULT NULL,
  `pain_details` varchar(1000) DEFAULT NULL,
  `fuctional_status_adls_iadls` varchar(1000) DEFAULT NULL,
  `nutrition_condition` varchar(1000) DEFAULT NULL,
  `communication_hearing_condition` varchar(1000) DEFAULT NULL,
  `psychological_social_condition` varchar(1000) DEFAULT NULL,
  `assessment_reason` varchar(255) NOT NULL,
  `assessment_reason_other` varchar(1500) DEFAULT NULL,
  `assessment_source_information` varchar(255) NOT NULL,
  `assessment_date` datetime NOT NULL,
  `discharge_date` datetime DEFAULT NULL,
  `discharge_reason` varchar(1500) DEFAULT NULL,
  `discharge_comments` varchar(1500) DEFAULT NULL,
  `state` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_review1` (`user_id`),
  KEY `fk_employee_reviewed1` (`employee_id`),
  CONSTRAINT `fk_employee_reviewed1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `fk_user_review1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `review` */

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `level` int(11) NOT NULL,
  `level_name` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `agency_id` int(10) unsigned NOT NULL,
  `position` varchar(255) NOT NULL,
  `manager_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `level_UNIQUE` (`level`),
  KEY `fk_role_agency1_idx` (`agency_id`),
  KEY `fk_position_manager` (`manager_id`),
  CONSTRAINT `fk_position_manager` FOREIGN KEY (`manager_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `fk_role_agency1` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role` */

/*Table structure for table `schema_version` */

DROP TABLE IF EXISTS `schema_version`;

CREATE TABLE `schema_version` (
  `version_rank` int(11) NOT NULL,
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) NOT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`version`),
  KEY `schema_version_vr_idx` (`version_rank`),
  KEY `schema_version_ir_idx` (`installed_rank`),
  KEY `schema_version_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `schema_version` */

insert  into `schema_version`(`version_rank`,`installed_rank`,`version`,`description`,`type`,`script`,`checksum`,`installed_by`,`installed_on`,`execution_time`,`success`) values (1,1,'1','create install schema','SQL','V1__create_install_schema.sql',-1106509354,'root','2017-07-29 02:19:53',8472,1),(2,2,'1.1','alter table id auto increment','SQL','V1_1__alter_table_id_auto_increment.sql',-757037675,'root','2017-07-29 02:19:58',5250,1),(10,10,'1.10','visit status change datatype selectedtable add','SQL','V1_10__visit_status_change_datatype_selectedtable_add.sql',716491245,'root','2017-07-29 02:20:07',1294,1),(11,11,'1.11','alter tables','SQL','V1_11__alter_tables.sql',-247943960,'root','2017-07-29 02:20:07',120,1),(12,12,'1.12','file upload create','SQL','V1_12__file_upload_create.sql',876211757,'root','2017-07-29 02:20:07',664,1),(13,13,'1.13','add missed columns review','SQL','V1_13__add_missed_columns_review.sql',-1282026214,'root','2017-07-29 02:20:18',10783,1),(14,14,'1.14','add homevisit and caregiver tables','SQL','V1_14__add_homevisit_and_caregiver_tables.sql',-528262379,'root','2017-07-29 02:20:19',643,1),(15,15,'1.15','alter table file upload id auto increment','SQL','V1_15__alter_table_file_upload_id_auto_increment.sql',44106782,'root','2017-07-29 02:20:20',659,1),(16,16,'1.16','add work item and relation with homevisit','SQL','V1_16__add_work_item_and_relation_with_homevisit.sql',1371482745,'root','2017-07-29 02:20:22',2111,1),(17,17,'1.18','add signature to visit and add table content','SQL','V1_18__add_signature_to_visit_and_add_table_content.sql',2135531500,'root','2017-07-29 02:20:23',1520,1),(3,3,'1.2','serviceplan','SQL','V1_2__serviceplan.sql',-1222425101,'root','2017-07-29 02:19:59',293,1),(18,18,'1.20','add id column and alter training has employee','SQL','V1_20__add_id_column_and_alter_training_has_employee.sql',-601009374,'root','2017-07-29 02:20:25',1698,1),(19,19,'1.21','add id column and alter employee has activity','SQL','V1_21__add_id_column_and_alter_employee_has_activity.sql',-838294653,'root','2017-07-29 02:20:27',1512,1),(20,20,'1.22','alter table content  rename columnname table','SQL','V1_22__alter_table_content__rename_columnname_table.sql',-359939850,'root','2017-07-29 02:20:27',120,1),(21,21,'1.23','alter table visit has activity  rename columnname table','SQL','V1_23__alter_table_visit_has_activity__rename_columnname_table.sql',-1963837765,'root','2017-07-29 02:20:27',148,1),(22,22,'1.24','drop pk serviceplan','SQL','V1_24__drop_pk_serviceplan.sql',1990598093,'root','2017-07-29 02:20:28',528,1),(23,23,'1.25','alter table user add token and role','SQL','V1_25__alter_table_user_add_token_and_role.sql',-852953714,'root','2017-07-29 02:20:29',832,1),(24,24,'1.26','add employee clocking tables','SQL','V1_26__add_employee_clocking_tables.sql',491938485,'root','2017-07-29 02:20:29',365,1),(25,25,'1.27','alter table employee add weekly hours worked column','SQL','V1_27__alter_table_employee_add_weekly_hours_worked_column.sql',1169201171,'root','2017-07-29 02:20:30',802,1),(26,26,'1.28','alter table role add position and manager','SQL','V1_28__alter_table_role_add_position_and_manager.sql',-360751973,'root','2017-07-29 02:20:31',928,1),(27,27,'1.29','alter table admin add menulist','SQL','V1_29__alter_table_admin_add_menulist.sql',-2096085755,'root','2017-07-29 02:20:32',796,1),(4,4,'1.3','employee review columns','SQL','V1_3__employee_review_columns.sql',-1633297396,'root','2017-07-29 02:19:59',663,1),(28,28,'1.30','alter table admin add uniquekeyindex username','SQL','V1_30__alter_table_admin_add_uniquekeyindex_username.sql',1502386157,'root','2017-07-29 02:20:32',291,1),(29,29,'1.31','add table health insurance claim','SQL','V1_31__add_table_health_insurance_claim.sql',-477179658,'root','2017-07-29 02:20:33',344,1),(30,30,'1.32','add table employee modify weeklyhourwoked','SQL','V1_32__add_table_employee_modify_weeklyhourwoked.sql',-1219110372,'root','2017-07-29 02:20:33',726,1),(31,31,'1.33','alter health insurance claim table','SQL','V1_33__alter_health_insurance_claim_table.sql',-61313074,'root','2017-07-29 02:20:40',6304,1),(32,32,'1.34','add fk reviewtable for user employee','SQL','V1_34__add_fk_reviewtable_for_user_employee.sql',466629262,'root','2017-07-29 02:20:41',1483,1),(33,33,'1.35','add fk parent key content','SQL','V1_35__add_fk_parent_key_content.sql',1467916156,'root','2017-07-29 02:20:42',752,1),(34,34,'1.37','add columns to health claim insurance','SQL','V1_37__add_columns_to_health_claim_insurance.sql',-1893974463,'root','2017-07-29 02:21:12',29540,1),(35,35,'1.38','add fk to health claim insurance','SQL','V1_38__add_fk_to_health_claim_insurance.sql',2032799834,'root','2017-07-29 02:21:15',2967,1),(36,36,'1.39','add fk document id user visit','SQL','V1_39__add_fk_document_id_user_visit.sql',630809852,'root','2017-07-29 02:21:28',13122,1),(5,5,'1.4','alter table serviceplan','SQL','V1_4__alter_table_serviceplan.sql',-46532266,'root','2017-07-29 02:20:00',1148,1),(37,37,'1.40','add language table','SQL','V1_40__add_language_table.sql',671232320,'root','2017-07-29 02:21:28',261,1),(6,6,'1.5','visit review columns','SQL','V1_5__visit_review_columns.sql',-2084313084,'root','2017-07-29 02:20:02',1548,1),(7,7,'1.6','drop table sinaturetype unused','SQL','V1_6__drop_table_sinaturetype_unused.sql',-1410366629,'root','2017-07-29 02:20:02',131,1),(8,8,'1.7','visit review columns2','SQL','V1_7__visit_review_columns2.sql',-255248793,'root','2017-07-29 02:20:05',2411,1),(9,9,'1.9','visit add serviceplan','SQL','V1_9__visit_add_serviceplan.sql',-238654323,'root','2017-07-29 02:20:05',486,1);

/*Table structure for table `serviceplan` */

DROP TABLE IF EXISTS `serviceplan`;

CREATE TABLE `serviceplan` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `approvedby` varchar(255) NOT NULL COMMENT 'No matter this plan is proved by one employee or not, the name of the person should be recorded here. ',
  `employee_id` int(10) unsigned DEFAULT NULL,
  `plan_start` datetime DEFAULT NULL,
  `plan_end` datetime DEFAULT NULL,
  `days` varchar(255) DEFAULT NULL COMMENT 'Record days in week for services.',
  `docurl` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_serviceplan_user1_idx` (`user_id`),
  KEY `fk_serviceplan_employee_idx` (`employee_id`),
  CONSTRAINT `fk_serviceplan_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `fk_serviceplan_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `serviceplan` */

/*Table structure for table `training` */

DROP TABLE IF EXISTS `training`;

CREATE TABLE `training` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `type` int(11) NOT NULL,
  `trainer` varchar(255) DEFAULT NULL,
  `location` varchar(255) NOT NULL,
  `note` varchar(1000) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `training` */

/*Table structure for table `training_has_employee` */

DROP TABLE IF EXISTS `training_has_employee`;

CREATE TABLE `training_has_employee` (
  `training_id` int(10) unsigned NOT NULL,
  `employee_id` int(10) unsigned NOT NULL,
  `notes` varchar(1000) DEFAULT NULL,
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `fk_training_has_employee_employee1_idx` (`employee_id`),
  KEY `fk_training_has_employee_training1_idx` (`training_id`),
  CONSTRAINT `fk_training_has_employee_employee1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `fk_training_has_employee_training1` FOREIGN KEY (`training_id`) REFERENCES `training` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `training_has_employee` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `company_id` int(10) unsigned DEFAULT NULL,
  `agency_id` int(10) unsigned DEFAULT NULL,
  `user_type` int(11) NOT NULL,
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
  `preferred_meal_id` int(10) unsigned DEFAULT NULL,
  `preferred_activity_id` int(10) unsigned DEFAULT NULL,
  `preferred_seat` varchar(255) DEFAULT NULL,
  `approvable_mail` int(11) DEFAULT NULL,
  `medicaid_no` varchar(255) NOT NULL,
  `medicare_no` varchar(255) DEFAULT NULL,
  `insurance` varchar(255) DEFAULT NULL,
  `insurance_start` datetime NOT NULL,
  `insurance_end` datetime NOT NULL,
  `insurance_eligiable` varchar(255) NOT NULL,
  `eligiable_start` datetime NOT NULL,
  `eligiable_end` datetime NOT NULL,
  `family_doctor` varchar(255) NOT NULL,
  `family_doctor_address` varchar(255) DEFAULT NULL,
  `family_doctor_tel` varchar(255) DEFAULT NULL,
  `expert_doctor` varchar(255) DEFAULT NULL,
  `expert_doctor_address` varchar(255) DEFAULT NULL,
  `expert_doctor_tel` varchar(255) DEFAULT NULL,
  `medical_condition` varchar(1000) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `vacation_note` varchar(1000) DEFAULT NULL,
  `vacation_start` datetime DEFAULT NULL,
  `vacation_end` datetime DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status_second` tinyint(4) DEFAULT NULL,
  `remember_token` varchar(255) DEFAULT NULL,
  `role_id` int(10) unsigned DEFAULT NULL,
  `PROFILE_PHOTO` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_last_name` (`agency_id`),
  KEY `fk_user_agency1_idx` (`agency_id`),
  KEY `fk_admin_role1_idx` (`role_id`),
  KEY `fk_profile_photo_idkey1` (`PROFILE_PHOTO`),
  KEY `fk_meal_user_idkey1` (`preferred_meal_id`),
  KEY `fk_activity_user_idkey1` (`preferred_activity_id`),
  CONSTRAINT `fk_activity_user_idkey1` FOREIGN KEY (`preferred_activity_id`) REFERENCES `activity` (`id`),
  CONSTRAINT `fk_meal_user_idkey1` FOREIGN KEY (`preferred_meal_id`) REFERENCES `meal` (`id`),
  CONSTRAINT `fk_profile_photo_idkey1` FOREIGN KEY (`PROFILE_PHOTO`) REFERENCES `file_upload` (`id`),
  CONSTRAINT `fk_user_agency1` FOREIGN KEY (`agency_id`) REFERENCES `agency` (`id`),
  CONSTRAINT `fk_user_role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table storing all customers. Holds foreign keys to the address table and the store table where this customer is registered.\n\nBasic information about the customer like first and last name are stored in the table itself. Same for the date the record was created and when the information was last updated.';

/*Data for the table `user` */

/*Table structure for table `visit` */

DROP TABLE IF EXISTS `visit`;

CREATE TABLE `visit` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `agency_id` int(10) unsigned NOT NULL,
  `check_in_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `selected_meal_id` int(10) unsigned DEFAULT NULL,
  `selected_seat` varchar(255) DEFAULT NULL,
  `user_barcode_id` varchar(255) DEFAULT NULL,
  `check_out_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `user_comments` varchar(1000) DEFAULT NULL,
  `notes` varchar(1000) DEFAULT NULL,
  `status` varchar(48) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` int(10) unsigned NOT NULL,
  `serviceplan_id` int(10) unsigned DEFAULT NULL,
  `selected_table` varchar(255) DEFAULT NULL,
  `signature` varchar(255) DEFAULT NULL,
  `USER_SIGNATURE` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`,`user_id`),
  KEY `fk_visit_user1_idx` (`user_id`),
  KEY `fk_usersign_visit_idkey2` (`USER_SIGNATURE`),
  CONSTRAINT `fk_usersign_visit_idkey2` FOREIGN KEY (`USER_SIGNATURE`) REFERENCES `file_upload` (`id`),
  CONSTRAINT `fk_visit_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `visit` */

/*Table structure for table `visit_has_activity` */

DROP TABLE IF EXISTS `visit_has_activity`;

CREATE TABLE `visit_has_activity` (
  `visit_id` int(10) unsigned NOT NULL,
  `activity_id` int(10) unsigned NOT NULL,
  `table_name` varchar(255) DEFAULT NULL,
  `seat` varchar(255) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  KEY `fk_visit_has_activity_activity1_idx` (`activity_id`),
  KEY `fk_visit_has_activity_visit1_idx` (`visit_id`),
  CONSTRAINT `fk_visit_has_activity_activity1` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`),
  CONSTRAINT `fk_visit_has_activity_visit1` FOREIGN KEY (`visit_id`) REFERENCES `visit` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `visit_has_activity` */

/*Table structure for table `work_item` */

DROP TABLE IF EXISTS `work_item`;

CREATE TABLE `work_item` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_name` varchar(255) NOT NULL,
  `item_note` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' help on cooking, help on shopping, help on laundry, etc.';

/*Data for the table `work_item` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
