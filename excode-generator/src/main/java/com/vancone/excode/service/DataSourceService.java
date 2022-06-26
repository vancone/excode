package com.vancone.excode.service;

import com.vancone.cloud.common.model.ResponsePage;
import com.vancone.excode.enums.ProjectEnum;
import com.vancone.excode.exception.ResponseException;
import com.vancone.excode.entity.DataSource;
import com.vancone.excode.enums.DataCarrier;
import com.vancone.excode.repository.DataSourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.sql.DriverManager;
import java.util.List;
import java.util.Optional;

/**
 * @author Tenton Lien
 * @since 2021/06/13
 */
@Slf4j
@Service
public class DataSourceService {

    @Autowired
    private DataSourceRepository dataSourceRepository;

    public void save(DataSource dataSource) {
        dataSourceRepository.save(dataSource);
    }

    public void delete(String dataSourceId) {
        dataSourceRepository.deleteById(dataSourceId);
    }

    public DataSource query(String id) {
        Optional<DataSource> optional = dataSourceRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ResponseException(ProjectEnum.DATA_SOURCE_NOT_EXIST);
        }
        return optional.get();
    }

    public ResponsePage<DataSource> queryPage(int pageNo, int pageSize, String search, String type) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedTime");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return new ResponsePage<>(dataSourceRepository.findAll(pageable));
    }

    public List<DataSource> queryList(DataCarrier type) {
        DataSource example = new DataSource();
        example.setType(type);
        return dataSourceRepository.findAll(Example.of(example));
    }

    public void testConnection(DataSource dataSource) {
        log.info("Test data source connection: {}", dataSource);
        if (dataSource == null) {
            throw new ResponseException(ProjectEnum.DATA_SOURCE_NOT_EXIST);
        }
        if (dataSource.getType() == DataCarrier.REDIS) {
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
        } else if (dataSource.getType() == DataCarrier.MYSQL) {
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
