package com.vancone.excode;

import com.vancone.excode.config.property.SystemConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Tenton Lien
 */
@SpringBootApplication
@EnableEurekaClient
@EnableConfigurationProperties(value = { SystemConfig.class })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("BCrypt of 'test': " + new BCryptPasswordEncoder().encode("test"));
    }
}