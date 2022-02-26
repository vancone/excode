package com.vancone.excode.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Tenton Lien
 * @since 2022/02/26
 */
@Data
public class GlobalConfig {

    @JsonProperty("redis")
    private RedisConfig redisConfig;

    @Data
    public static class RedisConfig {
        private String host;
        private int port = 6379;
        private String password;
    }
}
