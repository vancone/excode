package com.vancone.excode.generator.service;

import com.vancone.cloud.common.model.ResponsePage;
import com.vancone.excode.generator.entity.DataStoreNew;
import com.vancone.excode.generator.entity.DataStoreRelational;
import com.vancone.excode.generator.entity.DataStoreRelationalColumn;
import com.vancone.excode.generator.repository.DataStoreRelationalColumnRepository;
import com.vancone.excode.generator.repository.DataStoreRelationalRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Tenton Lien
 * @since 2022/05/09
 */
@Slf4j
@Service
public class DataStoreRelationalColumnService {

    @Autowired
    private DataStoreRelationalColumnRepository dataStoreRelationalColumnRepository;

    public DataStoreRelationalColumn create(DataStoreRelationalColumn column) {
        return dataStoreRelationalColumnRepository.save(column);
    }

    public void update(DataStoreRelationalColumn dataStore) {
        dataStoreRelationalColumnRepository.save(dataStore);
    }

    public DataStoreRelationalColumn query(String id) {
        return dataStoreRelationalColumnRepository.findById(id).get();
    }

    public ResponsePage<DataStoreRelationalColumn> queryPage(int pageNo, int pageSize, String search, String dataStoreRelationalId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        DataStoreRelationalColumn example = new DataStoreRelationalColumn();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("id", ExampleMatcher.GenericPropertyMatcher::contains);
        if (StringUtils.isNotBlank(search)) {
            example.setId(search);
        }
        DataStoreRelational dataStoreRelational = new DataStoreRelational();
        dataStoreRelational.setId(dataStoreRelationalId);
        example.setDataStoreRelational(dataStoreRelational);
        return new ResponsePage<>(dataStoreRelationalColumnRepository.findAll(Example.of(example, matcher), pageable));
    }

    public void delete(String id) {
        dataStoreRelationalColumnRepository.deleteById(id);
    }
}
