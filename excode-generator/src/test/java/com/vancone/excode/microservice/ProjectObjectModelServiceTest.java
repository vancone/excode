package com.vancone.excode.microservice;

import com.vancone.excode.entity.Output;
import com.vancone.excode.service.microservice.ProjectObjectModelService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProjectObjectModelServiceTest {

    @Autowired
    private ProjectObjectModelService projectObjectModelService;

    @Test
    public void generateDefaultPom() {
        Output output = projectObjectModelService.generate(SpringBootMicroserviceTest.microservice);
        Assert.assertEquals("VanCone Example/pom.xml", output.getPath());
        Assert.assertEquals("<?xml version='1.0' encoding='UTF-8'?>\r\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\r\n" +
                "  <modelVersion>4.0.0</modelVersion>\r\n" +
                "  <parent>\r\n" +
                "    <groupId>org.springframework.boot</groupId>\r\n" +
                "    <artifactId>spring-boot-starter-parent</artifactId>\r\n" +
                "    <version>2.6.3</version>\r\n" +
                "    <relativePath/>\r\n" +
                "  </parent>\r\n" +
                "  <groupId>com.vancone</groupId>\r\n" +
                "  <artifactId>example</artifactId>\r\n" +
                "  <version>1.0.0</version>\r\n" +
                "  <build>\r\n" +
                "    <plugins>\r\n" +
                "      <plugins>\r\n" +
                "        <groupId>org.springframework.boot</groupId>\r\n" +
                "        <artifactId>spring-boot-maven-plugin</artifactId>\r\n" +
                "      </plugins>\r\n" +
                "    </plugins>\r\n" +
                "  </build>\r\n" +
                "  <dependencies>\r\n" +
                "    <dependency>\r\n" +
                "      <groupId>com.vancone</groupId>\r\n" +
                "      <artifactId>vancone-cloud-common</artifactId>\r\n" +
                "      <version>0.1.0</version>\r\n" +
                "      <label>default</label>\r\n" +
                "    </dependency>\r\n" +
                "    <dependency>\r\n" +
                "      <groupId>org.springframework.boot</groupId>\r\n" +
                "      <artifactId>spring-boot-starter-web</artifactId>\r\n" +
                "      <label>default</label>\r\n" +
                "    </dependency>\r\n" +
                "    <dependency>\r\n" +
                "      <groupId>org.springframework.boot</groupId>\r\n" +
                "      <artifactId>spring-boot-starter-test</artifactId>\r\n" +
                "      <scope>test</scope>\r\n" +
                "      <exclusions>\r\n" +
                "        <exclusions>\r\n" +
                "          <groupId>org.junit.vintage</groupId>\r\n" +
                "          <artifactId>junit-vintage-engine</artifactId>\r\n" +
                "        </exclusions>\r\n" +
                "      </exclusions>\r\n" +
                "      <label>default</label>\r\n" +
                "    </dependency>\r\n" +
                "    <dependency>\r\n" +
                "      <groupId>org.mybatis.spring.boot</groupId>\r\n" +
                "      <artifactId>mybatis-spring-boot-starter</artifactId>\r\n" +
                "      <version>2.1.1</version>\r\n" +
                "      <label>mybatis</label>\r\n" +
                "    </dependency>\r\n" +
                "    <dependency>\r\n" +
                "      <groupId>mysql</groupId>\r\n" +
                "      <artifactId>mysql-connector-java</artifactId>\r\n" +
                "      <scope>runtime</scope>\r\n" +
                "      <label>mybatis</label>\r\n" +
                "    </dependency>\r\n" +
                "    <dependency>\r\n" +
                "      <groupId>com.github.pagehelper</groupId>\r\n" +
                "      <artifactId>pagehelper-spring-boot-starter</artifactId>\r\n" +
                "      <version>1.2.13</version>\r\n" +
                "      <label>mybatis</label>\r\n" +
                "    </dependency>\r\n" +
                "  </dependencies>\r\n" +
                "</project>\r\n", output.getContent());
    }
}