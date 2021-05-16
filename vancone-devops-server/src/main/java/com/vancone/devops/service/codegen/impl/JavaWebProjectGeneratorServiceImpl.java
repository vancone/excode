package com.vancone.devops.service.codegen.impl;

import com.vancone.devops.codegen.model.project.Project;
import com.vancone.devops.entity.VO.DirectoryVO;
import com.vancone.devops.service.codegen.JavaWebProjectGeneratorService;
import org.springframework.stereotype.Service;

/**
 * @author Tenton Lien
 * @date 3/14/2021
 */
@Service
public class JavaWebProjectGeneratorServiceImpl implements JavaWebProjectGeneratorService {
    @Override
    public DirectoryVO generateAll(Project project) {
        return null;
    }

    @Override
    public DirectoryVO generateControllers(Project project) {
        return null;
    }
}
