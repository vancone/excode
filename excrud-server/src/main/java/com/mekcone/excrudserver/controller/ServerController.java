package com.mekcone.excrudserver.controller;

import com.mekcone.excrudserver.repository.ProjectRepository;
import com.mekcone.excrudserver.repository.ServerRepository;
import com.mekcone.webplatform.common.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/excrud/server")
public class ServerController {
    @Autowired
    private ServerRepository serverRepository;

    @GetMapping("/{serverId}")
    public Response retrieve(@PathVariable String serverId) {
        return Response.success(serverRepository.findAll());
    }

    @GetMapping
    public Response retrieveList() {
        return Response.success(serverRepository.findAll());
    }
}
