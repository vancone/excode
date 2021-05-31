//package com.vancone.devops.config.security;
//
//import com.vancone.devops.service.basic.UserService;
//import com.vancone.devops.entity.DTO.User;
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//
///*
// * Author: Tenton Lien
// * Date: 10/1/2020
// */
//
//@Component
//@AllArgsConstructor
//public class WebContext {
//    private static final String USER_ATTRIBUTES = "USER_ATTRIBUTES";
//    private final UserService userService;
//
//
//    public void setCurrentUser(User user) {
//        RequestContextHolder.currentRequestAttributes().setAttribute(USER_ATTRIBUTES, user, RequestAttributes.SCOPE_REQUEST);
//    }
//
//    public User getCurrentUser() {
//        User user = (User) RequestContextHolder.currentRequestAttributes().getAttribute(USER_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
//        if (null != user) {
//            return user;
//        } else {
//            UserDetails springUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            if (null == springUser) {
//                return null;
//            }
//            user = userService.findUserByUsername(springUser.getUsername());
//            if (null != user) {
//                setCurrentUser(user);
//            }
//            return user;
//        }
//    }
//}
