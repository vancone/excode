package com.vancone.excode.generator.enums;

/**
 * @author Tenton Lien
 * @date 6/13/2021
 */
public enum DataSourceType {

    /**
     * Relational database
     */
    MYSQL,
    POSTGRESQL,

    /**
     * NoSQL database
     */
    MONGODB,
    REDIS,

    /**
     * Message broker
     */
    KAFKA,
    RABBITMQ,
    ROCKETMQ,

    /**
     * Search engine
     */
    ELASTICSEARCH
}
