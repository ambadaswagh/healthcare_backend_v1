ALTER TABLE `healthcare`.`appointment`
ADD COLUMN `alert_type` INT(11) NULL AFTER `updated_at`;