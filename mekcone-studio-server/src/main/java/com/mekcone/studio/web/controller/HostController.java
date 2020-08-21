package com.mekcone.studio.web.controller;

import com.mekcone.studio.web.repository.HostRepository;
import com.mekcone.webplatform.common.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/excrud/host")
public class HostController {
    @Autowired
    private HostRepository hostRepository;

    @GetMapping("/{hostId}")
    public Response retrieve(@PathVariable String hostId) {
        return Response.success(hostRepository.findAll());
    }

    @GetMapping
    public Response retrieveList() {
        return Response.success(hostRepository.findAll());
    }
}
