package com.vancone.excode.service.basic;

import com.vancone.excode.entity.DTO.data.source.DataSource;
import com.vancone.excode.enums.ProjectEnum;
import com.vancone.excode.exception.ResponseException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tenton Lien
 * @date 6/13/2021
 */
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

    public Page<DataSource> queryList(int pageNo, int pageSize, String search) {
        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedTime");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Query query = Query.query(Criteria.where("deleted").is(false));
        if (StringUtils.isNotBlank(search)) {
            query.addCriteria(Criteria.where("name").regex(search));
        }
        long count = mongoTemplate.count(query, DataSource.class);
        List<DataSource> dataSources = mongoTemplate.find(query.with(pageable), DataSource.class);
        return new PageImpl<>(dataSources, pageable, count);
    }
}
