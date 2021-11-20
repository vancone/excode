package com.vancone.excode.generator.service;

import com.vancone.excode.generator.entity.DTO.DataStore;
import com.vancone.excode.generator.enums.DataStoreType;
import com.vancone.excode.generator.enums.ResponseEnum;
import com.vancone.excode.generator.exception.ResponseException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tenton Lien
 * @since 2021/11/18
 */
@Service
public class DataStoreService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(DataStore store) {
        if (StringUtils.isBlank(store.getProjectId())) {
            throw new ResponseException(ResponseEnum.DATA_STORE_PROJECT_REQUIRED);
        }
        if (store.getType() == null) {
            store.setType(DataStoreType.COLUMNAR);
        }
        if (StringUtils.isNotBlank(store.getId())) {
            store.setUpdatedTime(LocalDateTime.now());
        } else {
            store.setCreatedTime(LocalDateTime.now());
        }
        mongoTemplate.save(store);
    }

    public List<DataStore> queryList(String projectId) {
        Query query = Query.query(
                Criteria.where("projectId").is(projectId));
        return mongoTemplate.find(query, DataStore.class);
    }

    public DataStore query(String dataStoreId) {
        return mongoTemplate.findById(dataStoreId, DataStore.class);
    }

    public void delete(String dataStoreId) {
        DataStore dataStore = mongoTemplate.findById(dataStoreId, DataStore.class);
        if (dataStore != null) {
            mongoTemplate.save(dataStore, "history_data_store");
            mongoTemplate.remove(dataStore);
        }
    }
}