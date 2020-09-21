package com.mekcone.studio.service.web;

/*
 * Author: Tenton Lien
 * Date: 9/20/2020
 */

import com.mekcone.studio.entity.DTO.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    boolean login(HttpServletResponse httpServletResponse, User user);
    boolean logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
    boolean getLoginStatus(HttpServletRequest httpServletRequest);
    void createAccount(User user);
    User getAccount(String accountId);
    void updateAccount();
}
