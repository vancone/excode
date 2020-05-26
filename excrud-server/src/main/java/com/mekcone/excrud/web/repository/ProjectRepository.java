package com.mekcone.excrud.web.repository;

import com.mekcone.excrud.codegen.model.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveProject(Project project) {
        mongoTemplate.save(project);
    }

    public List<Project> findAll() {
        List<Project> project = mongoTemplate.findAll(Project.class, "project");
        return project;
    }

    public Project find(String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(projectId));
        List<Project> projects = mongoTemplate.find(query, Project.class);
        if (projects != null && !projects.isEmpty()) {
            return projects.get(0);
        } else {
            return null;
        }
    }

    public void delete(String projectId) {
        Query query = Query.query(Criteria.where("id").is(projectId));
        mongoTemplate.remove(query, Project.class);
    }
}
