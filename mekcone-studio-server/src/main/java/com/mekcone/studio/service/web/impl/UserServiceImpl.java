package com.mekcone.studio.service.web.impl;

import com.mekcone.studio.entity.DTO.User;
import com.mekcone.studio.service.web.UserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Author: Tenton Lien
 * Date: 9/20/2020
 */

@Service
public class UserServiceImpl implements UserService {
    @Override
    public boolean login(HttpServletResponse httpServletResponse, User user) {
        return false;
    }

    @Override
    public boolean logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return false;
    }

    @Override
    public boolean getLoginStatus(HttpServletRequest httpServletRequest) {
        return false;
    }

    @Override
    public void createAccount(User user) {

    }

    @Override
    public User getAccount(String accountId) {
        return null;
    }

    @Override
    public void updateAccount() {

    }
}
