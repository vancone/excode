package com.mekcone.studio.service;


import com.mekcone.studio.entity.Project;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;

public interface ProjectService {
    void create(Project project);
    Project findById(String projectId);
    Page<Project> findAll(int pageNo, int pageSize);
    void save(Project project);
    void delete(String projectId);
    void export(HttpServletResponse response, String fileType, String projectId);
}
