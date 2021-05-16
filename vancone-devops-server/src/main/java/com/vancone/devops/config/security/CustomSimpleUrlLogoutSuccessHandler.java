//package com.vancone.devops.config.security;
//
//import com.vancone.devops.enums.HttpStatusCode;
//import com.vancone.devops.service.basic.UserService;
//import com.vancone.devops.util.RestUtil;
//import com.vancone.devops.entity.DTO.User;
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///*
// * Author: Tenton Lien
// * Date: 10/1/2020
// */
//
//@Component
//@AllArgsConstructor
//public class CustomSimpleUrlLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
//
//    private final UserService userService;
//
//    @Override
//    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        if (authentication != null) {
//            User springUser = (User) authentication.getPrincipal();
//            if (null != springUser) {
//                User user = userService.findUserByUsername(springUser.getUsername());
//            }
//        }
//
//        RestUtil.response(response, HttpStatusCode.OK);
//    }
//}
