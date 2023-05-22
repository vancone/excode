package com.vancone.passport;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ExCode
 * @since 2023/05/22
 */
@SpringBootApplication
@MapperScan("com.vancone.passport.mapper")
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
