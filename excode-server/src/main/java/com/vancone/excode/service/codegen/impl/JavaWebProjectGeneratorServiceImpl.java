package com.vancone.excode.service.codegen.impl;

import com.vancone.excode.codegen.model.project.Project;
import com.vancone.excode.entity.VO.DirectoryVO;
import com.vancone.excode.service.codegen.JavaWebProjectGeneratorService;
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
