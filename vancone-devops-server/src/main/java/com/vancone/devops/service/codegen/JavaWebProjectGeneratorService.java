package com.vancone.devops.service.codegen;

import com.vancone.devops.codegen.model.project.Project;
import com.vancone.devops.entity.VO.DirectoryVO;

/**
 * @author Tenton Lien
 * @date 3/14/2021
 */
public interface JavaWebProjectGeneratorService {
    DirectoryVO generateAll(Project project);
    DirectoryVO generateControllers(Project project);

}
