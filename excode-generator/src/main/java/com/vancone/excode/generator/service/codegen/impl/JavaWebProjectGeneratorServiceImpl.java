package com.vancone.excode.generator.service.codegen.impl;

import com.vancone.excode.generator.codegen.model.project.Project;
import com.vancone.excode.generator.entity.VO.DirectoryVO;
import com.vancone.excode.generator.service.codegen.JavaWebProjectGeneratorService;
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
