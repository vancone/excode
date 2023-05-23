package ${template.properties.project.groupId}.${template.properties.project.artifactId};

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ExCode
 * @since ${date}
 */
@SpringBootApplication
@MapperScan("${template.properties.project.groupId}.${template.properties.project.artifactId}.mapper")
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
