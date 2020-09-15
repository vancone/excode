-- Author: Tenton Lien
-- Date: 9/10/2020

DROP TABLE IF EXISTS `extension`;

CREATE TABLE `extension` (
    `id` CHAR(32) PRIMARY KEY,
    `type` VARCHAR(50),
    `name` VARCHAR(50),
    `module_id` VARCHAR(50)
)