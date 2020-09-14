-- Author: Tenton Lien
-- Date: 9/10/2020

CREATE TABLE `project` (
    `id` VARCHAR(50),
    `name` VARCHAR(50),
    `description` TEXT,
    `created_time` TIMESTAMP,
    `modified_time` TIMESTAMP,
    `user_id` VARCHAR(50)
)