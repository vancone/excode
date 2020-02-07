package com.ttmek.autocrud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
public class LoginStatusService {

    @Autowired
    private static StringRedisTemplate stringRedisTemplate;

    public static String getUserId(HttpServletRequest httpServletRequest) {
        String sessionId = getSessionId(httpServletRequest);
        try {
            return stringRedisTemplate.opsForValue().get(sessionId) + "";
        } catch(Exception ex) {
            return null;
        }
    }

    public static String getSessionId(HttpServletRequest httpServletRequest) {
        return getCookie(httpServletRequest, "sessionId");
    }

    private static String getCookie(HttpServletRequest httpServletRequest, String key) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(key)) {
                    String value = cookie.getValue();
                    return value;
                }
            }
        }
        return null;
    }
}
