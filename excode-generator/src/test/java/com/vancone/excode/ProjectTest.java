package com.vancone.excode;

import com.vancone.excode.entity.Project;
import com.vancone.excode.entity.microservice.SpringBootMicroservice;
import com.vancone.excode.enums.MicroserviceType;
import com.vancone.excode.enums.OrmType;
import com.vancone.excode.service.ProjectService;
import com.vancone.excode.service.microservice.SpringBootMicroserviceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProjectTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SpringBootMicroserviceService springBootMicroserviceService;

    @Test
    public void createProject() {
        Project project = new Project();
        project.setName("Student Management System");
        project.setDescription("Developed for VanCone University");
        project = projectService.create(project);

        createSpringBootMicroservice(project);
    }

    private void createSpringBootMicroservice(Project project) {
        SpringBootMicroservice microservice = new SpringBootMicroservice();
        microservice.setType(MicroserviceType.SPRING_BOOT);
        microservice.setName("Student Management");
        microservice.setGroupId("edu.vancone");
        microservice.setArtifactId("student-management");
        microservice.setVersion("1.0.0");
        microservice.setServerPort(9090);
        microservice.setOrmType(OrmType.MYBATIS_ANNOTATION);
        microservice.setProject(project);
        springBootMicroserviceService.create(microservice);
    }
}
