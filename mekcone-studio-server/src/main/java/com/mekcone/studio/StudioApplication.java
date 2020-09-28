package com.mekcone.studio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
 * Author: Tenton Lien
 */

@SpringBootApplication
public class StudioApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudioApplication.class, args);
        System.out.println("BCrypt of 'test': " + new BCryptPasswordEncoder().encode("test"));
    }
}