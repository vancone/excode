package com.mekcone.studio.config.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.http.HttpServletRequest;

/*
 * Author: Tenton Lien
 * Date: 10/1/2020
 */

public class CustomTokenBasedRememberMeServices extends TokenBasedRememberMeServices {
    public CustomTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        return (boolean) request.getAttribute(DEFAULT_PARAMETER);
    }

}
