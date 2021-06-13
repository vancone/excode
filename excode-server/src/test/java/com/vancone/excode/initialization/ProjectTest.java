package com.vancone.excode.initialization;

import com.vancone.excode.entity.DTO.Project;
import com.vancone.excode.service.basic.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectTest {

    @Autowired
    private ProjectService projectService;

    @Test
    public void insertProjects() {
        Project project = new Project();
        project.setId("2c9720817973cd22017973cd57d90000");
        project.setName("VanCone Mall");
        project.setVersion("1.2.0");
        project.setOrganization("VanCone Corp");
        project.setDescription("Demo only");
        project.setModules(new ArrayList<>());
        projectService.save(project);
    }
}
