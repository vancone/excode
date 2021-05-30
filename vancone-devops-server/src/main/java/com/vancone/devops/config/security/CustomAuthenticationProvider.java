//package com.vancone.devops.config.security;
//
//import com.vancone.devops.enums.RoleEnum;
//import com.vancone.devops.enums.UserStatusEnum;
//import com.vancone.devops.service.basic.AuthenticationService;
//import com.vancone.devops.service.basic.UserService;
//import com.vancone.devops.entity.DTO.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.LockedException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
///*
// * Author: Tenton Lien
// * Date: 10/1/2020
// */
//
//@Component
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//    private final AuthenticationService authenticationService;
//    private final UserService userService;
//
//    @Autowired
//    public CustomAuthenticationProvider(AuthenticationService authenticationService, UserService userService) {
//        this.authenticationService = authenticationService;
//        this.userService = userService;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = (String) authentication.getCredentials();
//
//        User user = userService.findUserByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("Invalid username or password");
//        }
//
//        boolean result = authenticationService.authUser(user, username, password);
//        if (!result) {
//            throw new BadCredentialsException("Invalid username or password");
//        }
//
//        UserStatusEnum userStatusEnum = UserStatusEnum.fromCode(user.getStatus());
//        if (UserStatusEnum.Disable == userStatusEnum) {
//            throw new LockedException("Account frozen");
//        }
//
//        if (user.getRole() == null) {
//            user.setRole(RoleEnum.NORMAL.getCode());
//        }
//
//        User authUser = new User(user.getUsername(), user.getPassword());
//        return new UsernamePasswordAuthenticationToken(authUser, authUser.getPassword(), authUser.getAuthorities());
//    }
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return true;
//    }
//}