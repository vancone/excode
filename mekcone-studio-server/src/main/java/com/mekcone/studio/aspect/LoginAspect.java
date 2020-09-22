package com.mekcone.studio.aspect;

import com.mekcone.studio.constant.Constant;
import com.mekcone.studio.enums.UserEnum;
import com.mekcone.studio.exception.ResponseException;
import com.mekcone.studio.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Author: Tenton Lien
 * Date: 9/21/2020
 */

@Aspect
@Component
@Slf4j
public class LoginAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Before("execution(public * com.mekcone.studio.controller.*.*(..))")
    public void login() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        // Intercept request
        if(request.getMethod().equals("POST") && request.getServletPath().equals("/api/studio/user")){
            return;
        }

        // Fetch cookie
        Cookie cookie = CookieUtil.get(request, "token");
        if (cookie == null) {
            throw new ResponseException(UserEnum.NOT_LOGIN_YET);
        }

        // Find token in Redis
        String redis_token = redisTemplate.opsForValue().get(String.format(Constant.REDIS_TOKEN_PREFIX, cookie.getValue()));
        if (StringUtils.isEmpty(redis_token)){
            throw new ResponseException(UserEnum.LOGIN_TIMEOUT);
        }
    }
}