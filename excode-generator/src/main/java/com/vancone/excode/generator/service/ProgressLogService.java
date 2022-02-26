package com.vancone.excode.generator.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tenton Lien
 * @since 2022/02/26
 */
@Slf4j
@Service
public class ProgressLogService {

    private final String PROGRESS_PREFIX = "excode:progress:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public List<String> query(String projectId) {
        log.info("Query progress log of project(id = {})", projectId);
        return redisTemplate.opsForList().range(PROGRESS_PREFIX + projectId, 0, -1);
    }

    public void clear(String projectId) {
        redisTemplate.delete(PROGRESS_PREFIX + projectId);
    }

    public boolean isFinished(String projectId) {
        Long logRowCount = redisTemplate.opsForList().size(PROGRESS_PREFIX + projectId);
        if (logRowCount == null || logRowCount == 0) {
            return true;
        }
        String endLine = redisTemplate.opsForList().index(PROGRESS_PREFIX + projectId, logRowCount - 1);
        if (StringUtils.isNotBlank(endLine) && endLine.contains("<EOF>")) {
            return true;
        }
        return false;
    }
}
