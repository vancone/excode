package com.mekcone.excrudserver.service;

import com.mekcone.excrud.model.project.Project;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ProjectService {
    void exportProject(HttpServletResponse response, String fileType, String projectId);
    List<Project> retrieveList();
    void saveProject(Project project);
    void deleteProject(String projectId);
}
