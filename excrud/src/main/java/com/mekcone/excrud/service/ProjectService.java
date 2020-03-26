package com.mekcone.excrud.service;

import com.mekcone.excrud.model.project.Project;

public interface ProjectService {
    void generate();
    Project getProject();
    boolean load(String path);
    boolean output();
}
