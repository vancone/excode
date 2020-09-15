package com.mekcone.studio.service.impl;

import com.mekcone.studio.entity.Project;
import com.mekcone.studio.repository.ProjectRepository;
import com.mekcone.studio.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

/*
 * Author: Tenton Lien
 */

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public void create(Project project) {
        Date date = new Date();
        project.setCreatedTime(date);
        project.setModifiedTime(date);
        projectRepository.save(project);
    }

    @Override
    public Project findById(String projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        return project.orElse(null);
    }

    @Override
    public Page<Project> findAll(int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedTime");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return projectRepository.findAll(pageable);
    }

    @Override
    public void save(Project project) {
        Date date = new Date();
        project.setModifiedTime(date);
        projectRepository.save(project);
    }

    @Override
    public void delete(String projectId) {
        projectRepository.deleteById(projectId);
    }

    @Override
    public void generate(Project project) {

    }

    @Override
    public void export(HttpServletResponse response, String fileType, String projectId) {
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + projectId + ".xml");
        try {
            OutputStream outputStream = response.getOutputStream();
            Optional<Project> project = projectRepository.findById(projectId);
            if (project.isPresent()) {
                outputStream.write(project.toString().getBytes(StandardCharsets.UTF_8));
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
