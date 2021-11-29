package com.vancone.excode.generator.initialization;

import com.vancone.excode.generator.service.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectTest {

    @Autowired
    private ProjectService projectService;

    @Test
    public void insertProjects() {
    }

    @Test
    public void generateProject() {
        projectService.generate("2c9790817d1ed7dc017d1ef7e7c10001");
    }
}
