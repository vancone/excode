package com.vancone.excode.controller;

import com.vancone.cloud.common.model.Response;
import com.vancone.excode.entity.microservice.SpringBootMicroservice;
import com.vancone.excode.enums.TemplateType;
import com.vancone.excode.service.microservice.SpringBootMicroserviceService;
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
public class SpringBootMicroserviceController {

    @Autowired
    private SpringBootMicroserviceService springBootMicroserviceService;

    @PostMapping
    public Response create(@RequestBody SpringBootMicroservice microservice) {
        log.info("Create project: {}", microservice.toString());
        return Response.success(springBootMicroserviceService.create(microservice));
    }

    @GetMapping("structure")
    public Response queryStructure(@RequestParam String microserviceId) {
        return Response.success(springBootMicroserviceService.queryStructure(microserviceId));
    }

    @GetMapping("{id}")
    public Response query(@PathVariable String id) {
        SpringBootMicroservice microservice = springBootMicroserviceService.query(id);
        if (microservice != null) {
            log.info("Query microservice: {}", microservice.toString());
        }
        return Response.success(microservice);
    }

    @GetMapping
    public Response queryPage(@RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(defaultValue = "10") int pageSize,
                              @RequestParam(required = false) String search,
                              @RequestParam String projectId) {
        return Response.success(springBootMicroserviceService.queryPage(pageNo - 1, pageSize, search, projectId));
    }

    @PutMapping
    public Response update(@RequestBody SpringBootMicroservice microservice) {
        springBootMicroserviceService.update(microservice);
        return Response.success();
    }

    @DeleteMapping("{id}")
    public Response delete(@PathVariable String id) {
        springBootMicroserviceService.delete(id);
        return Response.success();
    }

    @GetMapping("generate")
    public Response generate(@RequestParam String microserviceId,
                             @RequestParam(required = false) TemplateType type,
                             @RequestParam(required = false) String name) {
        return Response.success(springBootMicroserviceService.generate(microserviceId, type, name));
    }
}
