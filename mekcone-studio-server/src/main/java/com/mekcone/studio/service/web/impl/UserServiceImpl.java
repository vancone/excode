package com.mekcone.studio.service.web.impl;

import com.mekcone.studio.entity.DTO.User;
import com.mekcone.studio.repository.UserRepository;
import com.mekcone.studio.service.web.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Author: Tenton Lien
 * Date: 9/20/2020
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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

    @Override
    public User findUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("Username is blank");
        }
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not exist");
        }

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        return new User(user.getUsername(), user.getPassword());
    }
}
