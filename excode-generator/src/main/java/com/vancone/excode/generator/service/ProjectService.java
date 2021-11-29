package com.vancone.excode.generator.service;

import com.vancone.excode.core.ProjectWriter;
import com.vancone.excode.core.model.Module;
import com.vancone.excode.core.model.Project;
import com.vancone.excode.core.model.datasource.MysqlDataSource;
import com.vancone.excode.generator.entity.DataStore;
import com.vancone.excode.generator.entity.ResponsePage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Service
public class ProjectService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(Project project) {
        mongoTemplate.save(project);
    }

    public Project query(String projectId) {
        return mongoTemplate.findById(projectId, Project.class);
    }

    public ResponsePage<Project> queryPage(int pageNo, int pageSize, String search) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedTime");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Query query = new Query();
        if (StringUtils.isNotBlank(search)) {
            query.addCriteria(Criteria.where("name").regex(search));
        }
        long count = mongoTemplate.count(query, Project.class);
        List<Project> projects = mongoTemplate.find(query.with(pageable), Project.class);
        return new ResponsePage<>(new PageImpl<>(projects, pageable, count));
    }

    public void delete(String projectId) {
        Project project = mongoTemplate.findById(projectId, Project.class);
        if (project != null) {
            project.setUpdatedTime(LocalDateTime.now());
            mongoTemplate.save(project, "deleted_project");
            mongoTemplate.remove(project);
        }
    }

    public void generate(String projectId) {
        Project project = mongoTemplate.findById(projectId, Project.class);
        if (project != null) {
            com.vancone.excode.core.model.Project finalProject = new com.vancone.excode.core.model.Project();
            Module module = new Module();
            module.setType("spring-boot");
            module.setEnable(true);
            module.getProperties().put("ormType", "MYBATIS_ANNOTATION");
            List<Module> modules = new ArrayList<>();
            modules.add(module);
            finalProject.setModules(modules);

            com.vancone.excode.core.model.Project.DataSource dataSource = new com.vancone.excode.core.model.Project.DataSource();
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            MysqlDataSource.Connection connection = new MysqlDataSource.Connection();
            connection.setHost("10.10.10.2");
            connection.setPassword("VaNcOnE_MaRiAdB");
            mysqlDataSource.setConnection(connection);
            dataSource.setMysql(mysqlDataSource);
            finalProject.setDatasource(dataSource);

            for (DataStore dataStore: mongoTemplate.find(
                    Query.query(Criteria.where("projectId").is(project.getId())), DataStore.class)) {
                MysqlDataSource.Table table = new MysqlDataSource.Table();
                mysqlDataSource.getTables().add(table);
                table.setName(dataStore.getName());
                table.setDescription(dataStore.getDescription());
                List<MysqlDataSource.Table.Column> columns = new ArrayList<>();
                table.setColumns(columns);
                table.setPrimaryKeyName("id");
                for (DataStore.Node node: dataStore.getNodes()) {
                    MysqlDataSource.Table.Column column = new MysqlDataSource.Table.Column();
                    column.setName(node.getName());
                    column.setType(node.getType());
                    column.setLength(node.getLength());
                    if ("id".equals(column.getName())) {
                        column.setPrimaryKey(true);
                    }
                    columns.add(column);
                }
            }

            finalProject.setGroupId("com.vancone");
            finalProject.setArtifactId("app-manager");
            ProjectWriter writer = new ProjectWriter(finalProject);
            writer.write();
        }
    }
}
