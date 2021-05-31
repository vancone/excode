package com.vancone.excode.service.codegen;

import com.vancone.excode.codegen.model.project.Project;
import com.vancone.excode.entity.VO.DirectoryVO;

/**
 * @author Tenton Lien
 * @date 3/14/2021
 */
public interface JavaWebProjectGeneratorService {
    DirectoryVO generateAll(Project project);
    DirectoryVO generateControllers(Project project);

}
