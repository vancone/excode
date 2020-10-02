package com.mekcone.studio;

import com.mekcone.studio.config.property.SystemConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
 * Author: Tenton Lien
 */

@SpringBootApplication
@EnableConfigurationProperties(value = { SystemConfig.class })
public class StudioApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudioApplication.class, args);
        System.out.println("BCrypt of 'test': " + new BCryptPasswordEncoder().encode("test"));
    }
}