package com.vancone.devops.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tenton Lien
 * @date 9/12/2020
 */
@RestController
@RequestMapping("/api/devops/user")
public class UserController {

    @GetMapping("info")
    public String getInfo() {
        return "{\n" +
                "    \"id\": \"4291d7da9005377ec9aec4a71ea837f\",\n" +
                "    \"name\": \"天野远子\",\n" +
                "    \"username\": \"admin\",\n" +
                "    \"password\": \"\",\n" +
                "    \"avatar\": \"/avatar2.jpg\",\n" +
                "    \"status\": 1,\n" +
                "    \"telephone\": \"\",\n" +
                "    \"lastLoginIp\": \"27.154.74.117\",\n" +
                "    \"lastLoginTime\": 1534837621348,\n" +
                "    \"creatorId\": \"admin\",\n" +
                "    \"createTime\": 1497160610259,\n" +
                "    \"merchantCode\": \"TLif2btpzg079h15bk\",\n" +
                "    \"deleted\": 0,\n" +
                "    \"roleId\": \"admin\",\n" +
                "    \"role\": {}\n" +
                "  }";
    }

//    @Autowired
//    private UserService userService;
//
//    @GetMapping
//    public Response getLoginStatus(HttpServletRequest httpServletRequest) {
//        userService.getLoginStatus(httpServletRequest);
//        return Response.success("already login");
//    }
//
//    @PostMapping
//    public Response login(HttpServletResponse httpServletResponse, @RequestBody User user) {
////        userService.login(httpServletResponse, user);
//        return Response.success(userService.findUserByUsername(user.getUsername()));
//    }
//
//    @DeleteMapping
//    public Response logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
//        userService.logout(httpServletRequest, httpServletResponse);
//        return Response.success();
//    }
}
