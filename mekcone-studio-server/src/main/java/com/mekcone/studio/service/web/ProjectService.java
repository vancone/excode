package com.mekcone.studio.service.web;


import com.mekcone.studio.entity.DTO.Project;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;

/*
 * Author: Tenton Lien
 */

public interface ProjectService {
    void create(Project project);
    Project findById(String projectId);
    Page<Project> findAll(int pageNo, int pageSize);
    void save(Project project);
    void delete(String projectId);
    void generate(Project project);
    void export(HttpServletResponse response, String fileType, String projectId);
}
