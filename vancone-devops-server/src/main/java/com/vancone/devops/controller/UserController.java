//package com.vancone.devops.controller;
//
//import com.vancone.cloud.common.model.Response;
//import com.vancone.devops.entity.DTO.User;
//import com.vancone.devops.service.basic.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @author Tenton Lien
// * @date 9/12/2020
// */
//@RestController
//@RequestMapping("/api/devops/user")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping
//    public Response getLoginStatus(HttpServletRequest httpServletRequest) {
//        userService.getLoginStatus(httpServletRequest);
//        return Response.success("already login");
//    }
//
//    @PostMapping
//    public Response login(HttpServletResponse httpServletResponse, @RequestBody User user) {
////        userService.login(httpServletResponse, user);
//        return Response.success(userService.findUserByUsername(user.getUsername()));
//    }
//
//    @DeleteMapping
//    public Response logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
//        userService.logout(httpServletRequest, httpServletResponse);
//        return Response.success();
//    }
//}
