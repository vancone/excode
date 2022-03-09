package com.vancone.excode.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Tenton Lien
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.vancone.excode" })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}