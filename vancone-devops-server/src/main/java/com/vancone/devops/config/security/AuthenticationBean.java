package com.vancone.devops.config.security;

import lombok.Data;

/**
 * @author Tenton Lien
 * @date 10/1/2020
 */
@Data
public class AuthenticationBean {
    private String username;
    private String password;
    private boolean remember;
}
