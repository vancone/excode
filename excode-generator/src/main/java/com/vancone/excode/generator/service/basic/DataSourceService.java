package com.vancone.excode.generator.service.basic;

import com.vancone.excode.generator.entity.DTO.data.source.DataSource;
import com.vancone.excode.generator.enums.DataSourceType;
import com.vancone.excode.generator.enums.ProjectEnum;
import com.vancone.excode.generator.exception.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tenton Lien
 * @date 6/13/2021
 */
@Slf4j
@Service
public class DataSourceService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(DataSource dataSource) {
        LocalDateTime time = LocalDateTime.now();
        if (dataSource.getId() == null) {
            dataSource.setCreatedTime(time);
        }
        dataSource.setModifiedTime(time);
        mongoTemplate.save(dataSource);
    }

    public DataSource query(String id) {
        DataSource dataSource = mongoTemplate.findById(id, DataSource.class);
        if (dataSource == null || dataSource.getDeleted()) {
            throw new ResponseException(ProjectEnum.DATA_SOURCE_NOT_EXIST);
        }
        return dataSource;
    }

    public Page<DataSource> queryPage(int pageNo, int pageSize, String search, String type) {
        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedTime");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Query query = Query.query(Criteria.where("deleted").is(false));
        if (StringUtils.isNotBlank(search)) {
            query.addCriteria(Criteria.where("name").regex(search));
        }

        if (StringUtils.isNotBlank(type)) {
            query.addCriteria(Criteria.where("type").is(type));
        }

        long count = mongoTemplate.count(query, DataSource.class);
        List<DataSource> dataSources = mongoTemplate.find(query.with(pageable), DataSource.class);
        return new PageImpl<>(dataSources, pageable, count);
    }

    public void testConnection(String id) {
        log.info("Test data source connection: {}", id);
        DataSource dataSource = query(id);
        if (dataSource == null) {
            throw new ResponseException(ProjectEnum.DATA_SOURCE_NOT_EXIST);
        }
        if (dataSource.getType() == DataSourceType.REDIS) {
            try {
                Jedis jedis = new Jedis(dataSource.getHost(), dataSource.getPort());
                if (StringUtils.isNotBlank(dataSource.getPassword())) {
                    jedis.auth(dataSource.getPassword());
                }
                String pong = jedis.ping();
                log.info("Redis reply: {}", pong);
                if (!"PONG".equals(pong)) {
                    throw new ResponseException(ProjectEnum.TEST_CONNECTION_FAILED);
                }
            } catch (JedisConnectionException e) {
                log.error("Connect Redis failed: {}", e.toString());
                throw new ResponseException(ProjectEnum.TEST_CONNECTION_FAILED);
            }
        } else if (dataSource.getType() == DataSourceType.MYSQL) {
            try {
                String url = "jdbc:mysql://" + dataSource.getHost() + ":" + dataSource.getPort() + "/" + dataSource.getDatabase();
                DriverManager.getConnection(url, dataSource.getUsername(), dataSource.getPassword());
            } catch (Exception e) {
                log.error("Connect MySQL failed: {}", e.toString());
                throw new ResponseException(ProjectEnum.TEST_CONNECTION_FAILED);
            }
        } else {
            throw new ResponseException(ProjectEnum.TEST_CONNECTION_FAILED);
        }
    }
}
