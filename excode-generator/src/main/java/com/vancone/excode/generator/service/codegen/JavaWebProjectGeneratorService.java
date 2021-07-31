package com.vancone.excode.generator.service.codegen;

import com.vancone.excode.generator.codegen.model.project.Project;
import com.vancone.excode.generator.entity.VO.DirectoryVO;

/**
 * @author Tenton Lien
 * @date 3/14/2021
 */
public interface JavaWebProjectGeneratorService {
    DirectoryVO generateAll(Project project);
    DirectoryVO generateControllers(Project project);

}
