package com.mekcone.excrud.web.service.impl;

import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.web.repository.ProjectRepository;
import com.mekcone.excrud.web.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public void exportProject(HttpServletResponse response, String fileType, String projectId) {
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + projectId + ".xml");
        try {
            OutputStream outputStream = response.getOutputStream();
            Project project = projectRepository.find(projectId);
            if (project != null) {
                outputStream.write(project.toXMLString().getBytes("utf-8"));
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Project> retrieveList() {
        return projectRepository.findAll();
    }

    @Override
    public void saveProject(Project project) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        project.setModifiedTime(simpleDateFormat.format(date));
        projectRepository.saveProject(project);
    }

    @Override
    public void deleteProject(String projectId) {
        projectRepository.delete(projectId);
    }
}
