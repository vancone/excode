package com.mekcone.studio.config.security;

import com.mekcone.studio.config.property.CookieConfig;
import com.mekcone.studio.util.JsonUtil;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/*
 * Author: Tenton Lien
 * Date: 10/1/2020
 */

public class CustomAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(CustomAuthenticationProcessingFilter.class);

    public CustomAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher("/api/user/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;
        try (InputStream is = request.getInputStream()) {
            AuthenticationBean authenticationBean = JsonUtil.toJsonObject(is, AuthenticationBean.class);
            request.setAttribute(TokenBasedRememberMeServices.DEFAULT_PARAMETER, authenticationBean.isRemember());
            authRequest = new UsernamePasswordAuthenticationToken(authenticationBean.getUsername(), authenticationBean.getPassword());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            authRequest = new UsernamePasswordAuthenticationToken("", "");
        }
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);

    }

    void setUserDetailsService(UserDetailsService userDetailsService) {
        CustomTokenBasedRememberMeServices tokenBasedRememberMeServices = new CustomTokenBasedRememberMeServices(CookieConfig.getName(), userDetailsService);
        tokenBasedRememberMeServices.setTokenValiditySeconds(CookieConfig.getInterval());
        setRememberMeServices(tokenBasedRememberMeServices);
    }

    private void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
