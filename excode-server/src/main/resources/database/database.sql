-- Author: Tenton Lien
-- Date: 9/10/2020

CREATE TABLE database (
    `id` CHAR(32) PRIMARY KEY,
    `type` VARCHAR(50),
    `name` VARCHAR(50),
    `host` VARCHAR(50),
    `port` INTEGER,
    `username` VARCHAR(50),
    `password` VARCHAR(50)
)