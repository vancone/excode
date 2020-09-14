package com.mekcone.studio.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/*
 * Author: Tenton Lien
 * Date: 9/12/2020
 */

@RestController
@RequestMapping("/api/studio/user")
public class MockController {

    @GetMapping
    public UserVO getUserInfo() {
        UserVO userVO = new UserVO();
        return userVO;
    }

    @Data
    public static class UserVO {
        private Result result = new Result();

        @Data
        static class Result {
            private Role role = new Role();
            private String roleId = "admin";
            private String id = "111111";
            private String name = "Name";
            private String username = "username";
            private String password = "";

            @Data
            class Role {
                private String id = "admin";
                private List<Permission> permissions = new ArrayList<>();

                {
                    permissions.add(new Permission("dashboard"));
                    permissions.add(new Permission("project"));
                    permissions.add(new Permission("module"));
                }

                @Data
                class Permission {
                    private String roleId = "admin";
                    private String permissionId;

                    public Permission(String permissionId) {
                        this.permissionId = permissionId;
                    }
                }
            }
        }
    }
}
