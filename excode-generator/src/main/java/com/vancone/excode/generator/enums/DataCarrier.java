package com.vancone.excode.generator.enums;

/**
 * @author Tenton Lien
 * @date 2021/06/13
 */
public enum DataCarrier {

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
