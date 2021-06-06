package com.vancone.excode.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 * @date 10/1/2020
 */
@Data
@Component
@ConfigurationProperties(prefix = "system")
public class SystemConfig {

    private List<String> securityIgnoreUrls = new ArrayList<>();

    private PasswordKeyConfig pwdKey;

    @Data
    public static class PasswordKeyConfig {
        private String publicKey;
        private String privateKey;
    }
}
