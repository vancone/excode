package com.vancone.excode.generator.entity.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vancone.excode.generator.codegen.util.StrUtil;
import com.vancone.excode.generator.entity.DTO.data.RootDataNode;
import com.vancone.excode.generator.entity.DTO.module.Module;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tenton Lien
 * @date 2020/09/09
 */
@Data
@Table
@Entity
@ApiModel("Low-Code Project")
@JsonInclude(JsonInclude.Include.NON_NULL)
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Project {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    private String name;

    private String version;

    private String description;

    private String author;

    private String organization;

    @CreationTimestamp
    @Column(updatable = false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
}
