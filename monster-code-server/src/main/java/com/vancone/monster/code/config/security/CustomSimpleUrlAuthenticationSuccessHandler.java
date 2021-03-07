package com.vancone.monster.code.config.security;

import com.vancone.monster.code.entity.DTO.User;
import com.vancone.monster.code.service.web.UserService;
import com.vancone.monster.code.enums.HttpStatusCode;
import com.vancone.monster.code.util.RestUtil;
import lombok.AllArgsConstructor;
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
