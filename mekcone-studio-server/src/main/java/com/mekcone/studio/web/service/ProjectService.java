package com.mekcone.studio.web.service;


import com.mekcone.studio.web.entity.Project;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ProjectService {
    void exportProject(HttpServletResponse response, String fileType, String projectId);
    Project retrieve(String projectId);
    List<Project> retrieveList();
    void saveProject(Project project);
    void deleteProject(String projectId);
}
