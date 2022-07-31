package com.vancone.excode.controller;

import com.vancone.web.common.model.Response;
import com.vancone.excode.entity.DataStoreRelationalColumn;
import com.vancone.excode.service.DataStoreRelationalColumnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tenton Lien
 * @since 2022/05/09
 */
@Slf4j
@RestController
@RequestMapping("/api/excode/data-store/relational/column")
public class DataStoreRelationalColumnController {

    @Autowired
    private DataStoreRelationalColumnService dataStoreRelationalColumnService;

    @PostMapping
    public Response create(@RequestBody DataStoreRelationalColumn column) {
        log.info("Create column: {}", column.toString());
        return Response.success(dataStoreRelationalColumnService.create(column));
    }

    @GetMapping("{id}")
    public Response query(@PathVariable String id) {
        DataStoreRelationalColumn column = dataStoreRelationalColumnService.query(id);
        if (column != null) {
            log.info("Query column: {}", column.toString());
        }
        return Response.success(column);
    }

    @GetMapping
    public Response queryPage(@RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(defaultValue = "10") int pageSize,
                              @RequestParam(required = false) String search,
                              @RequestParam String dataStoreRelationalId) {
        return Response.success(dataStoreRelationalColumnService.queryPage(pageNo - 1, pageSize, search, dataStoreRelationalId));
    }

    @PutMapping
    public Response update(@RequestBody DataStoreRelationalColumn column) {
        dataStoreRelationalColumnService.update(column);
        return Response.success();
    }

    @DeleteMapping("{id}")
    public Response delete(@PathVariable String id) {
        dataStoreRelationalColumnService.delete(id);
        return Response.success();
    }
}
