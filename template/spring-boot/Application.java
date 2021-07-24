package __groupId__.__artifactId__;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ExCode
 * @date __date__
 */
@SpringBootApplication
@MapperScan("__groupId__.__artifactId__.mapper")
public class __ArtifactId__Application {
  public static void main(String[] args) {
    SpringApplication.run(__ArtifactId__Application.class, args);
  }
}
