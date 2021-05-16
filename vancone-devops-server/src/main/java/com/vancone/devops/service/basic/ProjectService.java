package com.vancone.devops.service.basic;


import com.vancone.devops.entity.DTO.Project;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Tenton Lien
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
