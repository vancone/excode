package com.mekcone.excrud.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mekcone.excrud.codegen.constant.DataType;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.web.service.ProjectService;
import com.mekcone.webplatform.common.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/excrud/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/{projectId}")
    public Response retrieve(@PathVariable String projectId) {
        Project project = projectService.retrieve(projectId);
        log.info("Retrieve project: {}", project.toJSONString());
        return Response.success(project);
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
            log.info("Save imported project: {}", project.toJSONString());
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

    @GetMapping("/dbTypeList")
    public Response retrieveDatabaseTypeList() throws IllegalAccessException {
        // Todo: The list should be cached in Redis to optimize performance
        List<String> dbTypes = new ArrayList<>();
        Field[] fields = DataType.class.getFields();
        for (Field field: fields) {
            if (field.getName().matches("SQL_.*")) {
                dbTypes.add(field.get(DataType.class).toString());
            }
        }
        return Response.success(dbTypes);
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
