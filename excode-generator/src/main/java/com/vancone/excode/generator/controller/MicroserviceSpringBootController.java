package com.vancone.excode.generator.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vancone.cloud.common.model.Response;
import com.vancone.excode.generator.entity.MicroserviceSpringBoot;
import com.vancone.excode.generator.entity.ProjectStructure;
import com.vancone.excode.generator.enums.TemplateType;
import com.vancone.excode.generator.service.MicroserviceSpringBootService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@Slf4j
@RestController
@RequestMapping("/api/excode/microservice/spring-boot")
public class MicroserviceSpringBootController {

    @Autowired
    private MicroserviceSpringBootService microserviceSpringBootService;

    @PostMapping
    public Response create(@RequestBody MicroserviceSpringBoot microservice) {
        log.info("Create project: {}", microservice.toString());
        return Response.success(microserviceSpringBootService.create(microservice));
    }

    @GetMapping("structure")
    public Response queryStructure(@RequestParam String microserviceId) {
        return Response.success(microserviceSpringBootService.queryStructure(microserviceId));
    }

    @GetMapping("{id}")
    public Response query(@PathVariable String id) {
        MicroserviceSpringBoot microservice = microserviceSpringBootService.query(id);
        if (microservice != null) {
            log.info("Query microservice: {}", microservice.toString());
        }
        return Response.success(microservice);
    }

    @GetMapping
    public Response queryPage(@RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(defaultValue = "5") int pageSize,
                              @RequestParam(required = false) String search,
                              @RequestParam String projectId) {
        return Response.success(microserviceSpringBootService.queryPage(pageNo - 1, pageSize, search, projectId));
    }

    @PutMapping
    public Response update(@RequestBody MicroserviceSpringBoot microservice) {
        microserviceSpringBootService.update(microservice);
        return Response.success();
    }

    @DeleteMapping("{id}")
    public Response delete(@PathVariable String id) {
        microserviceSpringBootService.delete(id);
        return Response.success();
    }

    @GetMapping("generate")
    public Response generate(@RequestParam String microserviceId,
                             @RequestParam(required = false) TemplateType type) {
        return Response.success(microserviceSpringBootService.generate(microserviceId, type));
    }
}
