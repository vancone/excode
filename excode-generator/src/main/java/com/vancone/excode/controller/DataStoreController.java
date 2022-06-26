package com.vancone.excode.controller;

import com.vancone.cloud.common.model.Response;
import com.vancone.excode.entity.DataStoreRelational;
import com.vancone.excode.service.DataStoreRelationalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tenton Lien
 * @since 2022/05/09
 */
@Slf4j
@RestController
@RequestMapping("/api/excode/data-store/relational")
public class DataStoreController {

    @Autowired
    private DataStoreRelationalService dataStoreRelationalService;

    @PostMapping
    public Response create(@RequestBody DataStoreRelational dataStore) {
        log.info("Create dataStore: {}", dataStore.toString());
        return Response.success(dataStoreRelationalService.create(dataStore));
    }

    @GetMapping("{id}")
    public Response query(@PathVariable String id) {
        DataStoreRelational dataStore = dataStoreRelationalService.query(id);
        return Response.success(dataStore);
    }

    @GetMapping
    public Response queryPage(@RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(defaultValue = "5") int pageSize,
                              @RequestParam(required = false) String search,
                              @RequestParam String projectId) {
        return Response.success(dataStoreRelationalService.queryPage(pageNo - 1, pageSize, search, projectId));
    }

    @PutMapping
    public Response update(@RequestBody DataStoreRelational dataStore) {
        dataStoreRelationalService.update(dataStore);
        return Response.success();
    }

    @DeleteMapping("{id}")
    public Response delete(@PathVariable String id) {
        dataStoreRelationalService.delete(id);
        return Response.success();
    }

    @GetMapping("ddl/{dataStoreId}")
    public Response generateCode(@PathVariable String dataStoreId) {
        return Response.success(dataStoreRelationalService.generateDDL(dataStoreId));
    }
}
