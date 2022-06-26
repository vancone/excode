package com.vancone.excode.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Tenton Lien
 * @since 2022/02/26
 */
@Slf4j
@Service
public class ProgressLogService {

    private static final String LOG_PREFIX = "excode:progress:";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ");

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public List<String> query(String projectId) {
        log.info("Query progress log of project(id = {})", projectId);
        return redisTemplate.opsForList().range(LOG_PREFIX + projectId, 0, -1);
    }

    public void output(String projectId, String message) {
        redisTemplate.opsForList().rightPush(LOG_PREFIX + projectId, formatter.format(LocalDateTime.now()) + message);
    }

    public void clear(String projectId) {
        redisTemplate.delete(LOG_PREFIX + projectId);
    }

    public boolean isFinished(String projectId) {
        Long logRowCount = redisTemplate.opsForList().size(LOG_PREFIX + projectId);
        if (logRowCount == null || logRowCount == 0) {
            return true;
        }
        String endLine = redisTemplate.opsForList().index(LOG_PREFIX + projectId, logRowCount - 1);
        if (StringUtils.isNotBlank(endLine) && endLine.contains("<EOF>")) {
            return true;
        }
        return false;
    }
}
