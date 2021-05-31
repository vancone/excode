package com.vancone.excode.controller;

import com.vancone.cloud.common.model.Response;
import com.vancone.excode.entity.DTO.OfficialDocument;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tenton Lien
 */
@RestController
@RequestMapping("/api/devops/document")
public class DocumentController {

    @PostMapping
    public Response createOfficialDocument(@RequestBody OfficialDocument officialDocument) {
        return Response.success();
    }
}
