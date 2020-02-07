package com.ttmek.autocrud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ttmek.autocrud.mapper")
public class AutocrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutocrudApplication.class, args);
    }

}
