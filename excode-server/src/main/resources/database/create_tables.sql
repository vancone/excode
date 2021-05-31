-- Author: Tenton Lien
-- Date: 5/16/2021

DROP TABLE IF EXISTS `project`;

CREATE TABLE `project`
(
    `id`            VARCHAR(50) PRIMARY KEY,
    `name`          VARCHAR(50),
    `version`       VARCHAR(50),
    `description`   TEXT,
    `created_time`  TIMESTAMP,
    `modified_time` TIMESTAMP,
    `organization`  VARCHAR(255),
    `user_id`       VARCHAR(50)
);

DROP TABLE IF EXISTS `module`;

CREATE TABLE `module`
(
    `id`         VARCHAR(50) PRIMARY KEY,
    `type`       VARCHAR(50),
    `name`       VARCHAR(50),
    `project_id` VARCHAR(50),
    `properties` JSON
);

DROP TABLE IF EXISTS `module_type`;

CREATE TABLE `module_type`
(
    `id`          VARCHAR(50) PRIMARY KEY,
    `name`        JSON,
    `description` TEXT,
    `publisher`   VARCHAR(50)
);

SET @DEFAULT_PUBLISHER = 'MonCode';

INSERT INTO `module_type`
VALUES ('api_document', '{
  "en": "API Document",
  "zh-CN": "接口文档"
}', 'Supporting PDF / Markdown / HTML', @DEFAULT_PUBLISHER),
       ('spring_boot_microservice', '{
         "en": "Spring Boot Microservice",
         "zh-CN": "Spring Boot 微服务"
       }', '', @DEFAULT_PUBLISHER);


DROP TABLE IF EXISTS `extension`;

CREATE TABLE `extension`
(
    `id`        VARCHAR(50) PRIMARY KEY,
    `type`      VARCHAR(50),
    `name`      VARCHAR(50),
    `module_id` VARCHAR(50)
);

DROP TABLE IF EXISTS `document`;

CREATE TABLE `document`
(
    `id`           VARCHAR(50) PRIMARY KEY,
    `title`        VARCHAR(500),
    `author`       VARCHAR(100),
    `created_time` TIMESTAMP,
    `content`      TEXT
);