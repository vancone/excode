package com.vancone.excode.microservice;

import com.vancone.excode.entity.Output;
import com.vancone.excode.entity.microservice.SpringBootMicroservice;
import com.vancone.excode.enums.OrmType;
import com.vancone.excode.service.microservice.SpringBootMicroserviceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringBootMicroserviceTest {
    @Autowired
    private SpringBootMicroserviceGenerator generator;

    public static SpringBootMicroservice microservice;

    static {
        microservice = new SpringBootMicroservice();
        microservice.setGroupId("com.vancone");
        microservice.setArtifactId("example");
        microservice.setVersion("1.0.0");
        microservice.setName("VanCone Example");
        microservice.setOrmType(OrmType.MYBATIS_ANNOTATION);
    }

    @Test
    public void applicationEntity() {
        Output output = generator.createApplicationEntry(microservice);
        Assert.assertEquals("VanCone Example/src/main/java/com/vancone/example/ExampleApplication.java", output.getPath());
    }


    @Test
    public void generateApplicationProperties() {
        Output output = generator.createProperty(microservice);
        Assert.assertEquals("VanCone Example/src/main/resources/application.properties", output.getPath());
        log.info(output.getContent());
    }

}
