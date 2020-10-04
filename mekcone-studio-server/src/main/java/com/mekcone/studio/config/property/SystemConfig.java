package com.mekcone.studio.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/*
 * Author: Tenton Lien
 * Date: 10/1/2020
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
