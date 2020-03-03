package com.mekcone.incrud.service;

import com.mekcone.incrud.model.project.ProjectModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public interface ProjectModelService {
    public ProjectModel getProjectModel();
    public boolean load();
    public boolean load(String path);
    public boolean output();
}
