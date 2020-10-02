package com.mekcone.studio.service.web.impl;

import com.mekcone.studio.config.property.SystemConfig;
import com.mekcone.studio.entity.DTO.User;
import com.mekcone.studio.service.web.AuthenticationService;
import com.mekcone.studio.service.web.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/*
 * Author: Tenton Lien
 * Date: 10/1/2020
 */

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;

    private final SystemConfig systemConfig;


    /**
     * @param username username
     * @param password password
     * @return boolean
     */
    @Override
    public boolean authUser(String username, String password) {
        User user = userService.findUserByUsername(username);
        return authUser(user, username, password);
    }


    @Override
    public boolean authUser(User user, String username, String rawPassword) {
        if (user == null) {
            return false;
        }
        String encodedPassword = user.getPassword();
        if (null == encodedPassword || encodedPassword.length() == 0) {
            return false;
        }

        log.info("encodedPassword={}, rawPassword={}", encodedPassword, rawPassword);
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }
}
