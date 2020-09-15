-- Author: Tenton Lien
-- Date: 9/14/2020

DROP TABLE IF EXISTS `module_type`;

CREATE TABLE `module_type` (
    `id` VARCHAR(50) PRIMARY KEY,
    `name` JSON,
    `description` TEXT,
    `publisher` VARCHAR(50)
);

SET @DEFAULT_PUBLISHER = 'MekCone Studio';

INSERT INTO `module_type` VALUES ('api_document', '{ "en": "API Document", "zh-CN": "接口文档" }', 'Supporting PDF / Markdown / HTML', @DEFAULT_PUBLISHER),
                                 ('spring_boot_microservice', '{"en": "Spring Boot Microservice", "zh-CN": "Spring Boot 微服务" }', '', @DEFAULT_PUBLISHER);
