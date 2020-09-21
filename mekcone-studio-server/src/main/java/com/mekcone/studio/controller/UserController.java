package com.mekcone.studio.controller;

import com.mekcone.studio.entity.DTO.User;
import com.mekcone.studio.entity.VO.Response;
import com.mekcone.studio.service.web.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Author: Tenton Lien
 * Date: 9/12/2020
 */

@RestController
@RequestMapping("/api/studio/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Response getLoginStatus(HttpServletRequest httpServletRequest) {
        userService.getLoginStatus(httpServletRequest);
        return Response.success("already login", null);
    }

    @PostMapping
    public Response login(HttpServletResponse httpServletResponse, @RequestBody User user) {
        userService.login(httpServletResponse, user);
        return Response.success();
    }

    @DeleteMapping
    public Response logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        userService.logout(httpServletRequest, httpServletResponse);
        return Response.success();
    }
}
