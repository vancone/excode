package com.vancone.excode.generator.service;

import com.vancone.cloud.common.model.ResponsePage;
import com.vancone.excode.generator.entity.*;
import com.vancone.excode.generator.repository.DataStoreRelationalRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Tenton Lien
 * @since 2022/05/09
 */
@Slf4j
@Service
public class DataStoreRelationalService {

    @Autowired
    private DataStoreRelationalRepository dataStoreRelationalRepository;

    public DataStoreNew create(DataStoreRelational dataStore) {
        return dataStoreRelationalRepository.save(dataStore);
    }

    public void update(DataStoreRelational dataStore) {
        dataStoreRelationalRepository.save(dataStore);
    }

    public DataStoreRelational query(String id) {
        return dataStoreRelationalRepository.findById(id).get();
    }

    public ResponsePage<DataStoreRelational> queryPage(int pageNo, int pageSize, String search, String projectId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        DataStoreRelational example = new DataStoreRelational();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains);
        if (StringUtils.isNotBlank(search)) {
            example.setName(search);
        }
        ProjectNew project = new ProjectNew();
        project.setId(projectId);
        example.setProject(project);
        return new ResponsePage<>(dataStoreRelationalRepository.findAll(Example.of(example, matcher), pageable));
    }

    public void delete(String id) {
        dataStoreRelationalRepository.deleteById(id);
    }

    public String generateDDL(String dataStoreId) {
        Optional<DataStoreRelational> optional = dataStoreRelationalRepository.findById(dataStoreId);
        if (optional.isPresent()) {
            DataStoreRelational dataStore = optional.get();
            String sql = "CREATE TABLE `" + dataStore.getName() + "` (\n";
            for (DataStoreRelationalColumn column: dataStore.getColumns()) {
                sql += "    `" + column.getName() + "` " + column.getType();
                if (column.getLength() != null && column.getLength() > 0) {
                    sql += "(" + column.getLength() + ")";
                }
                if (StringUtils.isNotBlank(column.getComment())) {
                    sql += " COMMENT '" + column.getComment() + "'";
                }
                sql += ",\n";
            }
            sql = sql.substring(0, sql.length() - 2) + "\n)";
            return sql;
        }
        return "";
    }
}
