package com.vancone.excode.core.config;

import com.vancone.excode.core.model.GlobalConfig;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

/**
 * @author Tenton Lien
 * @since 2022/02/26
 */
public class ConfigManager {

    private static GlobalConfig config;
    private static Jedis jedis;

    public static void load(GlobalConfig configInfo) {
        config = configInfo;
    }

    public static Jedis getJedis() {
        if (jedis == null) {
            GlobalConfig.RedisConfig redisConfig = config.getRedisConfig();
            jedis = new Jedis(redisConfig.getHost(), redisConfig.getPort());
            if (StringUtils.isNotBlank(redisConfig.getPassword())) {
                jedis.auth(redisConfig.getPassword());
            }
        }
        return jedis;
    }
}
