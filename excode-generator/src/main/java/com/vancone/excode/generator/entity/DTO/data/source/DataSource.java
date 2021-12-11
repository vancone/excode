package com.vancone.excode.generator.entity.DTO.data.source;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vancone.excode.core.enums.DataCarrier;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author Tenton Lien
 * @date 6/13/2021
 */
@Data
@Document("data_source")
public class DataSource {
    private String id;
    private String name;
    private DataCarrier type;
    private String host;
    private Integer port;
    private String username;
    private String password;
    private Boolean auth;
    private String database;
    private Boolean deleted = false;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedTime;
}
