package com.ttmek.autocrud.service;

import com.ttmek.autocrud.model.Project;

import java.util.List;

public interface ProjectsService {
    boolean create(Project project);
    List<Project> retrieveAll();
    List<Project> retrieve(String projectId);
    boolean update(Project project);
    boolean delete(String projectId);
}
