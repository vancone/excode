package com.vancone.monster.code.service.web;

/*
 * Author: Tenton Lien
 * Date: 9/20/2020
 */

import com.vancone.monster.code.entity.DTO.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService extends UserDetailsService {
    boolean login(HttpServletResponse httpServletResponse, User user);
    boolean logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    boolean getLoginStatus(HttpServletRequest httpServletRequest);
    void createAccount(User user);
    User getAccount(String accountId);
    void updateAccount();
    User findUserByUsername(String username);
}
