-- Author: Tenton Lien
-- Date: 9/10/2020

DROP TABLE IF EXISTS `module`;

CREATE TABLE module (
    `id` CHAR(32) PRIMARY KEY,
    `type` VARCHAR(50),
    `name` VARCHAR(50),
    `project_id` VARCHAR(50)
)