package com.vancone.monster.code.config.security;

import com.vancone.monster.code.enums.HttpStatusCode;
import com.vancone.monster.code.util.RestUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Author: Tenton Lien
 * Date: 10/1/2020
 */

@Component
public class CustomSimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        RestUtil.response(response, HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), exception.getMessage());
    }
}
