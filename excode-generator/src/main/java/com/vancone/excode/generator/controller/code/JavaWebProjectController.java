package com.vancone.excode.generator.controller.code;

import com.vancone.cloud.common.model.Response;
import com.vancone.excode.generator.codegen.model.project.Project;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tenton Lien
 * @date 3/14/2021
 */
@RestController
@RequestMapping("/api/devops/code/module/java-web-project")
public class JavaWebProjectController {

    @PostMapping
    public Response generateAll(@RequestBody Project project) {
        return Response.fail();
    }

    @PostMapping("controller")
    public Response generateController(@RequestBody Project project) {
        return Response.fail();
    }
}
