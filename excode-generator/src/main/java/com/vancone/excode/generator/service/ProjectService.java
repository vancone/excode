package com.vancone.excode.generator.service;

import com.vancone.excode.core.ProjectWriter;
import com.vancone.excode.core.model.DataStore;
import com.vancone.excode.core.model.Project;
import com.vancone.excode.generator.entity.ResponsePage;
import org.apache.commons.lang3.StringUtils;
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
    private ProjectService projectService;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(Project project) {
        mongoTemplate.save(project);
    }

    public Project query(String projectId) {
        Project project = mongoTemplate.findById(projectId, Project.class);
        if (project != null && project.getDataAccess() != null && project.getDataAccess().getDataStoreIds() != null) {
            List<DataStore> stores = new ArrayList<>();
            for (String dataStoreId : project.getDataAccess().getDataStoreIds()) {
                stores.add(mongoTemplate.findById(dataStoreId, DataStore.class));
            }
            project.getDataAccess().setDataStores(stores);
        }
        return project;
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
        Project project = query(projectId);
        if (project != null) {
            project.setUpdatedTime(LocalDateTime.now());
            mongoTemplate.save(project, "deleted_project");
            mongoTemplate.remove(project);
        }
    }

    public void generate(String projectId) {
        Project project = projectService.query(projectId);
        ProjectWriter writer = new ProjectWriter(project);
        writer.write();
    }
}
