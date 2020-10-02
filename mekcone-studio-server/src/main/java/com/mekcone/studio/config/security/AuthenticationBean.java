package com.mekcone.studio.config.security;

import lombok.Data;

/*
 * Author: Tenton Lien
 * Date: 10/1/2020
 */

@Data
public class AuthenticationBean {
    private String username;
    private String password;
    private boolean remember;
}
