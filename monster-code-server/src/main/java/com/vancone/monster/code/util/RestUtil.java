package com.vancone.monster.code.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vancone.monster.code.entity.VO.Response;
import com.vancone.monster.code.enums.HttpStatusCode;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Author: Tenton Lien
 * Date: 9/29/2020
 */

@Slf4j
public class RestUtil {

    public static void response(HttpServletResponse response, HttpStatusCode systemCode) {
        response(response, systemCode.getCode(), systemCode.getMessage());
    }

    public static void response(HttpServletResponse response, int systemCode, String msg) {
        response(response, systemCode, msg, null);
    }


    public static void response(HttpServletResponse httpServletResponse, int systemCode, String msg, Object content) {
        try {
            Response response = Response.fail(systemCode, msg);
            String responseJson = new ObjectMapper().writeValueAsString(response);
            httpServletResponse.setStatus(systemCode);
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.getWriter().write(responseJson);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
