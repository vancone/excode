package com.vancone.excode.generator.config;

import com.vancone.excode.core.config.ConfigManager;
import com.vancone.excode.core.model.GlobalConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Tenton Lien
 * @since 2022/02/26
 */
@Component
public class CoreConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @PostConstruct
    public void init() {
        GlobalConfig config = new GlobalConfig();

        // Config Redis connection
        GlobalConfig.RedisConfig redisConfig = new GlobalConfig.RedisConfig();
        redisConfig.setHost(redisHost);
        redisConfig.setPassword(redisPassword);
        config.setRedisConfig(redisConfig);

        ConfigManager.load(config);
    }
}
