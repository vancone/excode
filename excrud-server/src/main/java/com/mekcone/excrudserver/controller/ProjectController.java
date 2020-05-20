package com.mekcone.excrudserver.controller;

import com.mekcone.excrudserver.repository.ProjectRepository;
import com.mekcone.webplatform.common.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/excrud/project")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/{projectId}")
    public Response retrieve(@PathVariable String projectId) {
        return Response.success(projectRepository.findAll());
    }

    @GetMapping
    public Response retrieveList() {
        return Response.success(projectRepository.findAll());
    }

    @PostMapping("/login")
    public String fakeLogin() {
        return "{\"code\":0,\"data\":{\"token\":\"admin-token\"}}";
    }

    @GetMapping("/info")
    public String fakeUserInfo(@RequestParam String token) {
        if (token.equals("admin-token")) {
            return "{\"code\":0,\"data\":{\"roles\":[\"admin\"],\"introduction\":\"I am a super administrator\",\"avatar\":\"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif\",\"name\":\"Super Admin\"}}";
        }
        return "{\"websocket\":true,\"origins\":[\"*:*\"],\"cookie_needed\":false,\"entropy\":191921556}";
    }
}
