package com.ttmek.autocrud.service.impl;

import com.ttmek.autocrud.mapper.ProjectsMapper;
import com.ttmek.autocrud.model.Project;
import com.ttmek.autocrud.service.ProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectsServiceImpl implements ProjectsService {

    @Autowired
    private ProjectsMapper projectsMapper;

    @Override
    public boolean create(Project project) {
        projectsMapper.create(project);
        return true;
    }

    @Override
    public List<Project> retrieveAll() {
        List<Project> projectsList = projectsMapper.retrieveAll();
        return projectsList;
    }

    @Override
    public List<Project> retrieve(String projectId) {
        List<Project> projectsList = projectsMapper.retrieve(projectId);
        return projectsList;
    }

    @Override
    public boolean update(Project project) {
        projectsMapper.update(project);
        return true;
    }

    @Override
    public boolean delete(String projectId) {
        projectsMapper.delete(projectId);
        return true;
    }
}
