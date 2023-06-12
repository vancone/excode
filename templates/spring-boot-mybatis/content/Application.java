package ${template.properties.project.groupId}.${template.properties.project.artifactId};

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ExCode
 * @since ${date}
 */
@SpringBootApplication
@MapperScan("${template.properties.project.groupId}.${template.properties.project.artifactId}.mapper")
@ComponentScan(basePackages = "com.vancone")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
