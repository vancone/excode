package com.mekcone.studio.config.security;

import com.mekcone.studio.entity.DTO.User;
import com.mekcone.studio.enums.HttpStatusCode;
import com.mekcone.studio.service.web.UserService;
import com.mekcone.studio.util.RestUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
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
@AllArgsConstructor
public class CustomSimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User springUser = (User) authentication.getPrincipal();
        UserDetails user = userService.findUserByUsername(springUser.getUsername());;
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        RestUtil.response(response, HttpStatusCode.OK.getCode(), HttpStatusCode.OK.getMessage(), newUser);
    }
}
