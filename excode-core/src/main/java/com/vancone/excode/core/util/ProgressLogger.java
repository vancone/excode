package com.vancone.excode.core.util;

import com.vancone.excode.core.config.ConfigManager;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Tenton Lien
 * @since 2022/02/26
 */
public class ProgressLogger {

    private static Jedis jedis;

    private static final String LOG_PREFIX = "excode:progress:";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ");

    public static void output(String projectId, String message) {
        if (jedis == null || StringUtils.isBlank(jedis.ping())) {
            jedis = ConfigManager.getJedis();
        }
        jedis.rpush(LOG_PREFIX + projectId, formatter.format(LocalDateTime.now()) + message);
    }
}
