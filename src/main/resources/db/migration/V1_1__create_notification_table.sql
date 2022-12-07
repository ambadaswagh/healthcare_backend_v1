
DROP TABLE IF EXISTS `notification`;

CREATE TABLE `notification` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `agency_id` int(11) unsigned NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `email` varchar(1024) DEFAULT NULL,
  `sms` varchar(1024) DEFAULT NULL,
  `frequency` int(11) DEFAULT NULL,
  `notification_time` datetime DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
