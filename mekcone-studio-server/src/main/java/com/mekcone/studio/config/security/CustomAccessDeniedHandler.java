package com.mekcone.studio.config.security;

import com.mekcone.studio.enums.HttpStatusCode;
import com.mekcone.studio.util.RestUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Author: Tenton Lien
 * Date: 10/1/2020
 */

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        RestUtil.response(httpServletResponse, HttpStatusCode.BAD_GATEWAY);
    }
}
