ALTER TABLE `healthcare`.`agency` 
ADD COLUMN `main_white_label` VARCHAR(255) NULL AFTER `meals_selected`,
ADD COLUMN `upper_left_label` VARCHAR(255) NULL AFTER `main_white_label`,
ADD COLUMN `logo_id` INT(11) UNSIGNED NULL AFTER `upper_left_label`,
ADD INDEX `fk_agency_logo_idx` (`logo_id` ASC);
ALTER TABLE `healthcare`.`agency` 
ADD CONSTRAINT `fk_agency_logo`
  FOREIGN KEY (`logo_id`)
  REFERENCES `healthcare`.`file_upload` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;