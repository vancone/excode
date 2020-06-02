package com.mekcone.excrud.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.web.service.ProjectService;
import com.mekcone.webplatform.common.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/excrud/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/{projectId}")
    public Response retrieve(@PathVariable String projectId) {
        return Response.success();
    }

    @GetMapping
    public Response retrieveList() {
        return Response.success(projectService.retrieveList());
    }

    @PutMapping
    public Response update(@RequestBody Project project) {
        projectService.saveProject(project);
        return Response.success();
    }

    @PostMapping("/import")
    public Response importProject(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Response.fail(1, "File is empty");
        }

        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Project project = xmlMapper.readValue(new String(file.getBytes()), Project.class);
            projectService.saveProject(project);
            return Response.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail(2, "Import failed");
        }
    }

    @GetMapping("/download/{fileType}/{projectId}")
    public void downloadFile(HttpServletResponse response, @PathVariable String fileType, @PathVariable String projectId) {
        projectService.exportProject(response, fileType, projectId);
    }

    @DeleteMapping("/{projectId}")
    public Response deleteProject(@PathVariable String projectId) {
        projectService.deleteProject(projectId);
        return Response.success();
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
