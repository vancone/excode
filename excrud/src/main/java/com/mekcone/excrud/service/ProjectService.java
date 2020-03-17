package com.mekcone.excrud.service;

import com.mekcone.excrud.model.project.Project;

public interface ProjectService {
    Project getProject();
    boolean load();
    boolean load(String path);
    boolean output();
}
