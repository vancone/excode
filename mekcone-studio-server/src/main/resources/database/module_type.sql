-- Author: Tenton Lien
-- Date: 9/14/2020

DROP TABLE IF EXISTS `module_type`;

CREATE TABLE `module_type` (
    `id` VARCHAR(50) PRIMARY KEY,
    `name` VARCHAR(50),
    `description` TEXT,
    `publisher` VARCHAR(50)
);

SET @DEFAULT_PUBLISHER = 'MekCone Studio';

INSERT INTO `module_type` VALUES ('api_document', 'API Document', 'Supporting PDF / Markdown / HTML', @DEFAULT_PUBLISHER),
                                 ('spring_boot_microservice', 'Spring Boot Microservice', '', @DEFAULT_PUBLISHER);
