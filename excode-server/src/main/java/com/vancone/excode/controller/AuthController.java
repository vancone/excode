package com.vancone.excode.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tenton Lien
 */
@RestController
@RequestMapping("/api/devops/auth")
public class AuthController {

    @PostMapping("logout")
    public String logout() {
        return "{\n" +
                "    \"id\": Mock.mock(\"@guid\"),\n" +
                "    \"name\": \"@name\",\n" +
                "    \"username\": \"admin\",\n" +
                "    \"password\": \"\",\n" +
                "    \"avatar\": \"https://gw.alipayobjects.com/zos/rmsportal/jZUIxmJycoymBprLOUbT.png\",\n" +
                "    \"status\": 1,\n" +
                "    \"telephone\": \"\",\n" +
                "    \"lastLoginIp\": \"27.154.74.117\",\n" +
                "    \"lastLoginTime\": 1534837621348,\n" +
                "    \"creatorId\": \"admin\",\n" +
                "    \"createTime\": 1497160610259,\n" +
                "    \"deleted\": 0,\n" +
                "    \"roleId\": \"admin\",\n" +
                "    \"lang\": \"zh-CN\",\n" +
                "    \"token\": \"4291d7da9005377ec9aec4a71ea837f\"\n" +
                "  }";
    }
}
