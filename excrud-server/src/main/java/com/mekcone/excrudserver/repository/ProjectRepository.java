package com.mekcone.excrudserver.repository;

import com.mekcone.excrudserver.entity.ProjectRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ProjectRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveProject(ProjectRecord projectRecord) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        projectRecord.setUpdatedTime(simpleDateFormat.format(date));
        mongoTemplate.save(projectRecord);
    }

    public List<ProjectRecord> findAll() {
        Query query = new Query();
        List<ProjectRecord> projectRecords = mongoTemplate.findAll(ProjectRecord.class, "project");
        return projectRecords;
    }
}
