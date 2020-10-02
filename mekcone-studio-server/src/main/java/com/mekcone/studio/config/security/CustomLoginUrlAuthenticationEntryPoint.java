package com.mekcone.studio.config.security;

import com.mekcone.studio.enums.HttpStatusCode;
import com.mekcone.studio.util.RestUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Author: Tenton Lien
 * Date: 10/1/2020
 */

@Component
public final class CustomLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public CustomLoginUrlAuthenticationEntryPoint() {
        super("/api/user/login");
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        RestUtil.response(response, HttpStatusCode.UNAUTHORIZED);
    }

}
