package com.mekcone.studio.config;

import com.mekcone.studio.enums.HttpStatusCode;
import com.mekcone.studio.service.web.UserService;
import com.mekcone.studio.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Author: Tenton Lien
 * Date: 9/27/2020
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint = new CustomLoginUrlAuthenticationEntryPoint();

    @Autowired
    private UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().authenticationEntryPoint(loginUrlAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.PUT).permitAll()
                .antMatchers("/api/studio/user").permitAll()
                .antMatchers("/api/**").authenticated()
                .and()
                .csrf().disable();
    }


    public static class CustomLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

        public CustomLoginUrlAuthenticationEntryPoint() {
            super("/api/studio/login");
        }


        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                             AuthenticationException authException) {
            RestUtil.response(response, HttpStatusCode.UNAUTHORIZED);
        }

    }
}