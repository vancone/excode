package com.vancone.excode.entity.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vancone.excode.entity.DTO.module.Module;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tenton Lien
 * @date 9/9/2020
 */
@Data
@ApiModel("项目")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document
public class Project {

    private String id;

    private String name;

    private String description;

    private String version;

    private String organization;

    private Boolean deleted = false;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedTime;

    private Map<String, Module> modules = new HashMap<>();
}
