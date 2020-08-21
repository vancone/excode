package com.mekcone.studio;

import com.mekcone.studio.codegen.constant.UrlPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        // Check if environment variable exists
        if (UrlPath.EXCRUD_HOME.matches("null[/\\\\]?")) {
            log.error("Environment variable \"EXCRUD_HOME\" not set");
            System.exit(-1);
        }
        SpringApplication.run(ServerApplication.class, args);
    }
}