package __groupId__.__artifact.id__;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ExCode
 * @since __date__
 */
@SpringBootApplication
@MapperScan("__groupId__.__artifact.id__.mapper")
public class __ArtifactId__Application {
  public static void main(String[] args) {
    SpringApplication.run(__ArtifactId__Application.class, args);
  }
}
