package com.mekcone.excrudserver.repository;

import com.mekcone.excrudserver.entity.ProjectRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveProject(ProjectRecord projectRecord) {
        mongoTemplate.save(projectRecord);
    }

    public List<ProjectRecord> findAll() {
        Query query = new Query();
        List<ProjectRecord> projectRecords = mongoTemplate.findAll(ProjectRecord.class, "project");
        return projectRecords;
    }
}
